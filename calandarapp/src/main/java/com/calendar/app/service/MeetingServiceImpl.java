package com.calendar.app.service;

import com.calendar.app.dto.MeetingRequest;
import com.calendar.app.dto.MeetingResponse;
import com.calendar.app.dto.MeetingSearchParams;
import com.calendar.app.entity.Location;
import com.calendar.app.entity.Meeting;
import com.calendar.app.entity.User;
import com.calendar.app.exceptions.InvalidInputParameterException;
import com.calendar.app.exceptions.LocationNotFoundException;
import com.calendar.app.exceptions.MeetingNotFoundException;
import com.calendar.app.mapper.MeetingMapper;
import com.calendar.app.repository.LocationRepository;
import com.calendar.app.repository.MeetingRepository;
import com.calendar.app.repository.UserRepository;
import com.calendar.app.specification.MeetingSpecification;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.ZoneId.systemDefault;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final MeetingMapper meetingMapper;

    @Override
    public void createMeeting(MeetingRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        Set<User> participants = validateUsers(request, loggedUser);
        validateHours(request);
        saveMeeting(request, loggedUser, participants);
    }

    @Override
    public Set<MeetingResponse> getMeetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        String defaultTimeZone = loggedUser.getDefaultTimeZone();
        return meetingRepository.findAll()
                .stream()
                .filter(meeting -> meeting.getUsersWhoHasAccessToViewMeeting().contains(loggedUser))
                .map(meeting -> meetingMapper.toDTO(meeting, defaultTimeZone))
                .collect(Collectors.toSet());
    }

    @Override
    public MeetingResponse getMeetingById(String meetingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        String defaultTimeZone = loggedUser.getDefaultTimeZone();
        Meeting meeting = meetingRepository.findById(UUID.fromString(meetingId))
                .orElseThrow(MeetingNotFoundException::new);
        if (!meeting.getUsersWhoHasAccessToViewMeeting().contains(loggedUser))
            throw new MeetingNotFoundException();
        return meetingMapper.toDTO(meeting, defaultTimeZone);
    }

    @Override
    public Set<MeetingResponse> getMeetingBySearchParams(MeetingSearchParams request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
        String defaultTimeZone = loggedUser.getDefaultTimeZone();
        LocalDateTime start = request.getDay() != null ? request.getDay().atStartOfDay() : null;
        LocalDateTime end = request.getDay() != null ? request.getDay().plusDays(1).atStartOfDay() : null;

        UUID locationId = request.getLocation_id() != null ? UUID.fromString(request.getLocation_id()) : null;
        return meetingRepository.findAll(
                        MeetingSpecification.findByAgendaLike(request.getAgenda())
                                .and(MeetingSpecification.findByNameLike(request.getName()))
                                .and(MeetingSpecification.startDateFrom(start, defaultTimeZone))
                                .and(MeetingSpecification.endDateTo(end, defaultTimeZone))
                                .and(MeetingSpecification.locationIdEquals(locationId))
                                .and(MeetingSpecification.addressEquals(request.getAddress())))
                .stream()
                .filter(meeting -> meeting.getUsersWhoHasAccessToViewMeeting().contains(loggedUser))
                .map(meeting -> meetingMapper.toDTO(meeting, defaultTimeZone)).collect(Collectors.toSet());

    }

    private void validateHours(MeetingRequest request) {

        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();
        if (start.isBefore(LocalDateTime.now()))
            throw new InvalidInputParameterException("Start meeting can not be set in the past");
        if (end.isBefore(start))
            throw new InvalidInputParameterException("End meeting can not be set before start");
        final int HOURS = 8;
        if (end.minusHours(HOURS).isAfter(start))
            throw new InvalidInputParameterException("Meeting can not lasts longer than " + HOURS + " hours");

    }

    private Set<User> validateUsers(MeetingRequest request, User loggedUser) {
        Set<String> usersEmailsInSameCompanyAsLoggedUser = loggedUser.getCompany().getUsers()
                .stream().map(User::getEmail).collect(Collectors.toSet());
        log.debug("usersEmailsInSameCompanyAsLoggedUser {}", usersEmailsInSameCompanyAsLoggedUser);
        request.getParticipants().forEach(email -> {
            if (!usersEmailsInSameCompanyAsLoggedUser.contains(email))
                throw new UsernameNotFoundException("Participant user not found");
        });
        return userRepository.findByEmails(request.getParticipants());
    }

    private void saveMeeting(MeetingRequest request, User loggedUser, Set<User> users) {

        ZoneId defaultTimeZone = ZoneId.of(loggedUser.getDefaultTimeZone() != null ? loggedUser.getDefaultTimeZone() : String.valueOf(systemDefault()));

        ZonedDateTime zonedStartTime = request.getStart().atZone(defaultTimeZone);
        ZonedDateTime zonedEndTime = request.getEnd().atZone(defaultTimeZone);

        Meeting meeting = Meeting.builder()
                .owner(loggedUser)
                .name(request.getName())
                .agenda(request.getAgenda())
                .start(zonedStartTime)
                .end(zonedEndTime)
                .build();

        if (request.getLocationId() != null) {
            log.debug("locationId {}", request.getLocationId());
            Location location = locationRepository.findById(UUID.fromString(request.getLocationId()))
                    .orElseThrow(LocationNotFoundException::new);
            meeting.setLocation(location);
        }
        meeting = meetingRepository.save(meeting);
        savePerticipants(users, meeting);

    }

    private void savePerticipants(Set<User> users, Meeting meeting) {
        if (!users.isEmpty()) {
            users.forEach(user -> user.getMeeting().add(meeting));
            userRepository.saveAll(users);
        }
    }
}
