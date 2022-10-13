package com.calendar.app.service;

import com.calendar.app.dto.MeetingRequest;
import com.calendar.app.dto.MeetingResponse;
import com.calendar.app.dto.MeetingSearchParams;
import com.calendar.app.entity.Company;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;


public interface MeetingService {

    void createMeeting(MeetingRequest request);

    Set<MeetingResponse> getMeetings();

    MeetingResponse getMeetingById(String meetingId);

    Set<MeetingResponse> getMeetingBySearchParams(MeetingSearchParams request);
}
