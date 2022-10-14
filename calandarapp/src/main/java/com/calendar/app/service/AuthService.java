package com.calendar.app.service;

import com.calendar.app.dto.AuthRequest;
import com.calendar.app.dto.AuthResponse;
import com.calendar.app.entity.User;
import com.calendar.app.repository.UserRepository;

public interface AuthService {

    AuthResponse generateToken(AuthRequest request);
    User getLoggedUser();
}
