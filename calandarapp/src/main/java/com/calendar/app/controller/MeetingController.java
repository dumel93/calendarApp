package com.calendar.app.controller;


import com.calendar.app.dto.MeetingRequest;
import com.calendar.app.dto.MeetingResponse;
import com.calendar.app.dto.MeetingSearchParams;
import com.calendar.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createMeeting(@RequestBody @Valid MeetingRequest request) {
        log.info("createMeeting {}", request);
        meetingService.createMeeting(request);
    }

    @GetMapping
    public Set<MeetingResponse> getMeetings() {
        return meetingService.getMeetings();
    }

    @GetMapping("/{meetingId}")
    public MeetingResponse getMeetingById(@PathVariable String meetingId) {
        log.info("getMeetingById {}", meetingId);
        return meetingService.getMeetingById(meetingId);
    }

    @GetMapping("/query")
    public Set<MeetingResponse> getMeetingBySearchParams(@Valid MeetingSearchParams request) {
        log.info("getMeetingBySearchParams {}",request);
        return meetingService.getMeetingBySearchParams(request);
    }
}
