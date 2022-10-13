package com.calendar.app.repository;

import com.calendar.app.entity.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LocationRepository extends CrudRepository<Location, UUID> {

    Optional<Location> findById(UUID id);
    Set<Location> findAll();

}
