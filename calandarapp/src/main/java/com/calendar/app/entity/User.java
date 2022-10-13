package com.calendar.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "userId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID userId;

    @Size(max = 10)
    @Column(name = "position")
    private String position;

    @Size(max = 50)
    @Column(name = "user_name", unique = true)
    private String userName;

    @Size(max = 50)
    @Column(name = "email", unique = true)
    private String email;

    @Size(max = 100)
    @Column(name = "password")
    private String password;

    @Column(name = "default_time_zone")
    private String defaultTimeZone;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_meeting",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "meeting_id") }
    )
    private Set<Meeting> meeting = new HashSet<>();

}
