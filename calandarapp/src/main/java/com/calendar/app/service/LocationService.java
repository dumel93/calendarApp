package com.calendar.app.service;

import com.calendar.app.dto.LocationRequest;
import com.calendar.app.dto.LocationResponse;

import java.util.Set;

public interface LocationService {

    void createLocation(LocationRequest request);

    Set<LocationResponse> getLocations();

    LocationResponse getLocationById(String locationId);
}
