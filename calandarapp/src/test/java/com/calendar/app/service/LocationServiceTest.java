package com.calendar.app.service;

import com.calendar.app.dto.LocationResponse;
import com.calendar.app.entity.Location;
import com.calendar.app.exceptions.LocationNotFoundException;
import com.calendar.app.mapper.LocationMapper;
import com.calendar.app.repository.LocationRepository;
import com.calendar.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(classes = LocationServiceImpl.class)
class LocationServiceTest {

    @Autowired
    private LocationService locationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private LocationMapper locationMapper;
    @MockBean
    private AuthService authService;


    @Test
    void getLocations() {
        //given
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        LocationResponse locationResponse2 = new LocationResponse();
        locationResponse.setId(uuid2.toString());
        locationResponse.setName("test2");
        locationResponse.setAddress("test2");
        locationResponse.setName("test2");

        Set<LocationResponse> locationsResponse = new HashSet<>();
        locationsResponse.add(locationResponse);
        locationsResponse.add(locationResponse2);

        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");

        Location location2 = new Location();
        location2.setId(uuid2);
        location2.setName("test2");
        location2.setAddress("test2");

        Set<Location> locations = new HashSet<>();
        locations.add(location);
        locations.add(location2);
        when(locationRepository.findAll()).thenReturn(locations);
        when(locationMapper.toDTO(locations)).thenReturn(locationsResponse);

        // 	when
        Set<LocationResponse> result = locationService.getLocations();
        // 	then
        assertEquals(locationsResponse, result);
    }

    @Test
    void getLocationById() {
        //given
        UUID uuid = UUID.randomUUID();
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");

        Optional<Location> locationOptional = Optional.of(location);
        when(locationRepository.findById(uuid)).thenReturn(locationOptional);
        when(locationMapper.toDTO(locationOptional.get())).thenReturn(locationResponse);

        // 	when
        LocationResponse result = locationService.getLocationById(uuid.toString());
        // 	then
        assertEquals(locationResponse, result);
    }

    @Test
    void itShouldThrowLocationNotFoundException() {

        //given
        UUID uuid = UUID.randomUUID();
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");

        Optional<Location> locationOptional = Optional.of(location);
        when(locationRepository.findById(uuid)).thenReturn(locationOptional);
        when(locationMapper.toDTO(locationOptional.get())).thenReturn(locationResponse);

        // 	then
        assertThrows(LocationNotFoundException.class,
                () -> locationService.getLocationById(UUID.randomUUID().toString()));

    }
}