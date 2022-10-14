package com.calendar.app.service;

import com.calendar.app.dto.LocationResponse;
import com.calendar.app.dto.MeetingResponse;
import com.calendar.app.dto.MeetingSearchParams;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.Meeting;
import com.calendar.app.entity.User;
import com.calendar.app.exceptions.MeetingNotFoundException;
import com.calendar.app.mapper.MeetingMapper;
import com.calendar.app.repository.LocationRepository;
import com.calendar.app.repository.MeetingRepository;
import com.calendar.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MeetingServiceImpl.class)
class MeetingServiceTest {

    @Autowired
    private MeetingService meetingService;
    @MockBean
    private MeetingRepository meetingRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private MeetingMapper meetingMapper;
    @MockBean
    private AuthService authService;

    private final String defaultTimeZone = "Europe/Warsaw";
    private final String EMAIL = "test@gmail.com";

    @Test
    void getMeetings() {
        //given

        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        com.calendar.app.entity.User user = User.builder()
                .userId(UUID.randomUUID())
                .userName("userTest")
                .password("test123")
                .position("test")
                .email(EMAIL)
                .defaultTimeZone(defaultTimeZone)
                .build();
        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");
        location.setManager(user);

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setId(uuid.toString());
        meetingResponse.setName("test");
        meetingResponse.setLocation(locationResponse);
        meetingResponse.setParticipants(Collections.singleton(EMAIL));

        LocationResponse locationResponse2 = new LocationResponse();
        locationResponse2.setId(uuid.toString());
        locationResponse2.setName("test2");
        locationResponse2.setAddress("test2");
        locationResponse2.setName("test2");

        MeetingResponse meetingResponse2 = new MeetingResponse();
        meetingResponse2.setId(uuid2.toString());
        meetingResponse2.setName("test2");
        meetingResponse2.setLocation(locationResponse2);
        meetingResponse2.setParticipants(Collections.singleton(EMAIL));


        Set<MeetingResponse> meetingsResponse = new HashSet<>();
        meetingsResponse.add(meetingResponse);
        meetingsResponse.add(meetingResponse2);

        Meeting meeting = new Meeting();
        meeting.setId(uuid);
        meeting.setName("test");
        meeting.getParticipants().add(user);


        Meeting meeting2 = new Meeting();
        meeting2.setId(uuid2);
        meeting2.setName("test2");
        meeting2.getParticipants().add(user);

        List<Meeting> meetings = new ArrayList<>();
        meetings.add(meeting);
        meetings.add(meeting2);

        when(meetingRepository.findAll()).thenReturn(meetings);
        when(authService.getLoggedUser()).thenReturn(user);
        when(meetingMapper.toDTO(meeting, defaultTimeZone)).thenReturn(meetingResponse);
        when(meetingMapper.toDTO(meeting2, defaultTimeZone)).thenReturn(meetingResponse2);

        // 	when
        Set<MeetingResponse> result = meetingService.getMeetings();
        // 	then
        assertEquals(meetingsResponse, result);
        assertThat(result).hasSize(2);
    }


    @Test
    void failNoMeetingBySearchParamsByWrongLocationId() {
        //given
        String defaultTimeZone = "Europe/Warsaw";
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        final String EMAIL = "test@gmail.com";
        com.calendar.app.entity.User user = User.builder()
                .userId(UUID.randomUUID())
                .userName("userTest")
                .password("test123")
                .position("test")
                .email(EMAIL)
                .defaultTimeZone(defaultTimeZone)
                .build();
        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");
        location.setManager(user);

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setId(uuid.toString());
        meetingResponse.setName("test");
        meetingResponse.setLocation(locationResponse);
        meetingResponse.setParticipants(Collections.singleton(EMAIL));

        LocationResponse locationResponse2 = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test2");
        locationResponse.setAddress("test2");
        locationResponse.setName("test2");

        MeetingResponse meetingResponse2 = new MeetingResponse();
        meetingResponse2.setId(uuid2.toString());
        meetingResponse2.setName("test2");
        meetingResponse2.setLocation(locationResponse2);
        meetingResponse2.setParticipants(Collections.singleton(EMAIL));


        Set<MeetingResponse> meetingsResponse = new HashSet<>();
        meetingsResponse.add(meetingResponse);
        meetingsResponse.add(meetingResponse2);

        Meeting meeting = new Meeting();
        meeting.setId(uuid);
        meeting.setName("test");
        meeting.getParticipants().add(user);
        meeting.setStart(ZonedDateTime.now().plusDays(1));
        meeting.setEnd(ZonedDateTime.now().plusDays(2));


        Meeting meeting2 = new Meeting();
        meeting2.setId(uuid2);
        meeting2.setName("test2");
        meeting2.getParticipants().add(user);
        meeting2.setStart(ZonedDateTime.now().plusDays(2));
        meeting2.setEnd(ZonedDateTime.now().plusDays(3));

        List<Meeting> meetings = new ArrayList<>();
        meetings.add(meeting);
        meetings.add(meeting2);

        when(meetingRepository.findAll()).thenReturn(meetings);
        when(authService.getLoggedUser()).thenReturn(user);
        when(meetingMapper.toDTO(meeting, defaultTimeZone)).thenReturn(meetingResponse);
        when(meetingMapper.toDTO(meeting2, defaultTimeZone)).thenReturn(meetingResponse2);

        MeetingSearchParams request = new MeetingSearchParams();
        request.setLocation_id(UUID.randomUUID().toString());
        // 	when
        Set<MeetingResponse> result = meetingService.getMeetingBySearchParams(request);
        // 	then
        assertThat(result).hasSize(0);
        assertThat(result).doesNotContain(meetingResponse);
    }

    @Test
    void getNoMeetingBySearchByDayToday() {
        //given
        String defaultTimeZone = "Europe/Warsaw";
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        final String EMAIL = "test@gmail.com";
        com.calendar.app.entity.User user = User.builder()
                .userId(UUID.randomUUID())
                .userName("userTest")
                .password("test123")
                .position("test")
                .email(EMAIL)
                .defaultTimeZone(defaultTimeZone)
                .build();
        Location location = new Location();
        location.setId(uuid);
        location.setName("test");
        location.setAddress("test");
        location.setManager(user);

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test");
        locationResponse.setAddress("test");
        locationResponse.setName("test");

        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setId(uuid.toString());
        meetingResponse.setName("test");
        meetingResponse.setLocation(locationResponse);
        meetingResponse.setParticipants(Collections.singleton(EMAIL));

        LocationResponse locationResponse2 = new LocationResponse();
        locationResponse.setId(uuid.toString());
        locationResponse.setName("test2");
        locationResponse.setAddress("test2");
        locationResponse.setName("test2");

        MeetingResponse meetingResponse2 = new MeetingResponse();
        meetingResponse2.setId(uuid2.toString());
        meetingResponse2.setName("test2");
        meetingResponse2.setLocation(locationResponse2);
        meetingResponse2.setParticipants(Collections.singleton(EMAIL));


        Set<MeetingResponse> meetingsResponse = new HashSet<>();
        meetingsResponse.add(meetingResponse);
        meetingsResponse.add(meetingResponse2);

        Meeting meeting = new Meeting();
        meeting.setId(uuid);
        meeting.setName("test");
        meeting.getParticipants().add(user);
        meeting.setStart(ZonedDateTime.now().plusDays(1));
        meeting.setEnd(ZonedDateTime.now().plusDays(2));


        Meeting meeting2 = new Meeting();
        meeting2.setId(uuid2);
        meeting2.setName("test2");
        meeting2.getParticipants().add(user);
        meeting2.setStart(ZonedDateTime.now().plusDays(2));
        meeting2.setEnd(ZonedDateTime.now().plusDays(3));

        List<Meeting> meetings = new ArrayList<>();
        meetings.add(meeting);
        meetings.add(meeting2);

        when(meetingRepository.findAll()).thenReturn(meetings);
        when(authService.getLoggedUser()).thenReturn(user);
        when(meetingMapper.toDTO(meeting, defaultTimeZone)).thenReturn(meetingResponse);
        when(meetingMapper.toDTO(meeting2, defaultTimeZone)).thenReturn(meetingResponse2);

        MeetingSearchParams request = new MeetingSearchParams();
        request.setDay(LocalDate.now());
        // 	when
        Set<MeetingResponse> result = meetingService.getMeetingBySearchParams(request);
        // 	then
        assertThat(result).hasSize(0);
        assertThat(result).doesNotContain(meetingResponse);
    }

    @Test
    void itShouldThrowMeetingNotFoundException() {

        //given
        com.calendar.app.entity.User user = User.builder()
                .userId(UUID.randomUUID())
                .userName("userTest")
                .password("test123")
                .position("test")
                .email(EMAIL)
                .defaultTimeZone(defaultTimeZone)
                .build();
        UUID uuid = UUID.randomUUID();
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setId(uuid.toString());
        meetingResponse.setName("test");
        meetingResponse.setAgenda("test");

        Meeting meeting = new Meeting();
        meeting.setId(uuid);
        meeting.setName("test");
        meeting.setAgenda("test");

        Optional<Meeting> meetingOptional = Optional.of(meeting);
        when(authService.getLoggedUser()).thenReturn(user);
        when(meetingRepository.findById(uuid)).thenReturn(meetingOptional);
        when(meetingMapper.toDTO(meetingOptional.get(), defaultTimeZone)).thenReturn(meetingResponse);

        // 	then
        assertThrows(MeetingNotFoundException.class,
                () -> meetingService.getMeetingById(UUID.randomUUID().toString()));

    }
}