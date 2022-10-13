package com.calendar.app.service;

import com.calendar.app.dto.AuthRequest;
import com.calendar.app.dto.AuthResponse;

public interface AuthService {

    AuthResponse generateToken(AuthRequest request);
}
