package com.calendar.app.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meetings")
public class Meeting {

    @Id
    @Column(name = "meeting_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Size(max = 100)
    @Column(name = "name")
    private String name;

    @Size(max = 100)
    @Column(name = "agenda")
    private String agenda;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "start")
    private ZonedDateTime  start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "end")
    private ZonedDateTime end;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "meeting", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> participants = new HashSet<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public Set<User> getUsersWhoHasAccessToViewMeeting() {
        Set<User> viewers = new HashSet<>(this.participants);
        if (this.owner != null) viewers.add(this.owner);
        if (this.location != null && this.location.getManager() != null) viewers.add(this.location.getManager());
        return viewers;
    }
}
