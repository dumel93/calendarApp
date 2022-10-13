package com.calendar.app.controller;

import com.calendar.app.dto.AuthRequest;
import com.calendar.app.dto.AuthResponse;
import com.calendar.app.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/tokens")
public class AuthController {

    private final AuthServiceImpl authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AuthResponse generateToken(@RequestBody @NotNull @Valid AuthRequest request) throws AuthenticationException {
        log.info("generate Token call...");
        return authService.generateToken(request);
    }

}