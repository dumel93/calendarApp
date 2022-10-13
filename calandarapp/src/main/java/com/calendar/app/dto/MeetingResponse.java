package com.calendar.app.dto;

import com.calendar.app.entity.Location;
import com.calendar.app.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingResponse {

    private String id;
    private String name;
    private String agenda;
    private LocalDateTime start;
    private LocalDateTime end;
    @Builder.Default
    private Set<String> participants = new HashSet<>();
    private UserResponse owner;
    private LocationResponse location;
}
