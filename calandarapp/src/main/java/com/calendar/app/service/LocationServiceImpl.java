package com.calendar.app.service;


import com.calendar.app.dto.LocationRequest;
import com.calendar.app.dto.LocationResponse;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.User;
import com.calendar.app.exceptions.LocationNotFoundException;
import com.calendar.app.mapper.LocationMapper;
import com.calendar.app.repository.LocationRepository;
import com.calendar.app.repository.UserRepository;
import com.calendar.app.util.BasicAuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public void createLocation(LocationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        Location location = Location.builder()
                .id(UUID.randomUUID())
                .manager(loggedUser)
                .name(request.getName())
                .address(request.getAddress())
                .build();
        locationRepository.save(location);
    }

    @Override
    public Set<LocationResponse> getLocations() {
        return locationMapper.toDTO(locationRepository.findAll());
    }

    @Override
    public LocationResponse getLocationById(String locationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        Location location = locationRepository.findById(UUID.fromString(locationId))
                .orElseThrow(LocationNotFoundException::new);
        return locationMapper.toDTO(location);
    }
}
