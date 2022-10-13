package com.calendar.app.controller;

import com.calendar.app.dto.LocationRequest;
import com.calendar.app.dto.LocationResponse;
import com.calendar.app.service.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationServiceImpl locationServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createLocation(@RequestBody @Valid LocationRequest request) {
        log.info("createLocation {}", request);
        locationServiceImpl.createLocation(request);
    }

    @GetMapping
    public Set<LocationResponse> getLocations() {
        return locationServiceImpl.getLocations();
    }

    @GetMapping("/{locationId}")
    public LocationResponse getLocationById(@PathVariable String locationId) {
        log.info("createLocation {}", locationId);
        return locationServiceImpl.getLocationById(locationId);
    }
}
