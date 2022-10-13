package com.calendar.app.mapper;

import com.calendar.app.dto.LocationResponse;
import com.calendar.app.dto.MeetingResponse;
import com.calendar.app.dto.UserResponse;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.Meeting;
import com.calendar.app.entity.User;
import com.calendar.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.ZoneId.systemDefault;

@Component
@RequiredArgsConstructor
public class MeetingMapper {

    private final LocationMapper locationMapper;


    public MeetingResponse toDTO(Meeting entity, String defaultTimeZone) {

        ZoneId defaultTimeZoneId = ZoneId.of(defaultTimeZone != null ? defaultTimeZone : String.valueOf(systemDefault()));
        ZonedDateTime zonedStartTime = entity.getStart().withZoneSameInstant(defaultTimeZoneId);
        ZonedDateTime zonedEndTime = entity.getEnd().withZoneSameInstant(defaultTimeZoneId);

        return MeetingResponse.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .agenda(entity.getAgenda())
                .start(LocalDateTime.from(zonedStartTime))
                .end(LocalDateTime.from(zonedEndTime))
                .owner(UserResponse.builder()
                        .userId(entity.getId().toString())
                        .email(entity.getOwner().getEmail())
                        .position(entity.getOwner().getPosition())
                        .userName(entity.getOwner().getUserName())
                        .build()
                )
                .participants(entity.getParticipants()
                        .stream().map(User::getEmail).collect(Collectors.toSet()))
                .location(entity.getLocation() != null ? locationMapper.toDTO(entity.getLocation()): null)
                .build();
    }

}
