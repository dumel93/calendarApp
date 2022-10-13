package com.calendar.app.controller;


import com.calendar.app.dto.UserRequest;
import com.calendar.app.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUser(HttpServletRequest request, @RequestBody @NotNull  UserRequest user) throws AuthenticationException {
        userService.createUser(request, user);
    }
}
