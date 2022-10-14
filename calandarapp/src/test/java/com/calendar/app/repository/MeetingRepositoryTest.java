package com.calendar.app.repository;

import com.calendar.app.entity.Company;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.Meeting;
import com.calendar.app.entity.User;
import com.calendar.app.exceptions.LocationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    private final UUID uuid1 = UUID.fromString("a8949be7-4ba1-11ed-93cf-6c2b594a7bf6");
    private final UUID uuid2 = UUID.fromString("b8949be7-4ba1-11ed-93cf-6c2b594a7bf6");

    @BeforeEach
    void setUp() {
        injectedComponentsAreNotNull();
        prepareDefaultTestingData();
    }

    @Test
    public void should_find_locations_if_repository_is_not_empty() {
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        meetingRepository.deleteAll();
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


        Location location = new Location();

        location.setId(uuid1);
        location.setAddress("test");
        location.setName("test");
        location.setManager(user);

        location = locationRepository.save(location);

        Meeting meeting1 = Meeting.builder()
                .id(uuid1)
                .owner(user)
                .name("meeting1")
                .agenda("agenda1")
                .start(ZonedDateTime.now().plusDays(1))
                .end(ZonedDateTime.now().plusDays(1).plusHours(5))
                .location(location)
                .build();

        Meeting meeting2 = Meeting.builder()
                .id(uuid2)
                .owner(user)
                .name("meeting2")
                .agenda("agenda2")
                .start(ZonedDateTime.now().plusDays(2))
                .end(ZonedDateTime.now().plusDays(2).plusHours(5))
                .location(location)
                .build();

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
    }

    void injectedComponentsAreNotNull() {
        assertThat(locationRepository).isNotNull();
    }

    @Test
    void findAll() {

        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(2);
    }
}