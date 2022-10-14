package com.calendar.app.repository;

import com.calendar.app.entity.Company;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    private final UUID uuid1 = UUID.fromString("a8949be7-4ba1-11ed-93cf-6c2b594a7bf6");
    private final UUID uuid2 = UUID.fromString("b8949be7-4ba1-11ed-93cf-6c2b594a7bf6");
    private final UUID uuid3 = UUID.fromString("c8949be7-4ba1-11ed-93cf-6c2b594a7bf6");


    @BeforeEach
    void setUp() {
        injectedComponentsAreNotNull();
        prepareDefaultTestingData();
    }

    @Test
    public void should_find_locations_if_repository_is_not_empty() {
        Set<Location> locations = locationRepository.findAll();
        assertThat(locations).isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
    }

    private void prepareDefaultTestingData() {

        Company company = Company.builder().companyId(UUID.randomUUID()).name("test").build();
        company = companyRepository.save(company);
        com.calendar.app.entity.User user = User.builder()
                .userId(UUID.randomUUID())
                .userName("userTest")
                .password("test123")
                .position("test")
                .email("test@gmail.com")
                .defaultTimeZone("test")
                .company(company).build();
        user = userRepository.save(user);

        log.info("uuid " + uuid1);
        log.info("uuid " + uuid2);
        log.info("uuid " + uuid3);
        Location location = new Location();

        location.setId(uuid1);
        location.setAddress("test");
        location.setName("test");
        location.setManager(user);

        Location location2 = new Location();
        location2.setId(uuid2);
        location2.setAddress("test2");
        location2.setName("test2");
        location2.setManager(user);

        Location location3 = new Location();
        location3.setId(uuid3);
        location3.setAddress("test3");
        location3.setName("test3");
        location3.setManager(user);

        locationRepository.save(location);
        locationRepository.save(location2);
        locationRepository.save(location3);
    }

    void injectedComponentsAreNotNull() {
        assertThat(locationRepository).isNotNull();
    }

    @Test
    void findById() {
        Optional<Location> locationOptional = locationRepository.findById(uuid1);
        Location location = locationOptional.orElseThrow();
        assertThat(location).isNotNull();
        assertEquals(location.getId(), uuid1);
    }

    @Test
    void findAll() {
        Set<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(3);
    }
}