package com.calendar.app.mapper;

import com.calendar.app.dto.LocationResponse;
import com.calendar.app.dto.UserResponse;
import com.calendar.app.entity.Location;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LocationMapper implements DtoMapper<Location, LocationResponse> {


    @Override
    public LocationResponse toDTO(Location entity) {
        return LocationResponse.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .address(entity.getAddress())
                .manager(UserResponse.builder()
                        .userId(entity.getId().toString())
                        .email(entity.getManager().getEmail())
                        .position(entity.getManager().getPosition())
                        .userName(entity.getManager().getUserName())
                        .build()
                )
                .build();
    }

    @Override
    public Set<LocationResponse> toDTO(Set<Location> entities) {
        return DtoMapper.super.toDTO(entities);
    }
}
