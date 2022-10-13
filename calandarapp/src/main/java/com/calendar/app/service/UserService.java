package com.calendar.app.service;

import com.calendar.app.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void createUser(HttpServletRequest request, UserRequest userRequest);
}
