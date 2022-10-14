package com.calendar.app.service;

import com.calendar.app.config.DataBaseHolder;
import com.calendar.app.dto.UserRequest;
import com.calendar.app.entity.User;
import com.calendar.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    public void createUser(HttpServletRequest request, UserRequest userRequest) {

        User loggedUser = authService.getLoggedUser();
        TimeZone timeZone = RequestContextUtils.getTimeZone(request);
        ZoneId zoneId = timeZone != null ? timeZone.toZoneId() : ZoneId.systemDefault();
        User user = User.builder()
                .userId(UUID.randomUUID())
                .userName(userRequest.getUserName())
                .password(encoder.encode(userRequest.getPassword()))
                .position(userRequest.getPosition())
                .email(userRequest.getEmail())
                .defaultTimeZone(userRequest.getDefaultTimeZone() != null ? userRequest.getDefaultTimeZone() : zoneId.toString())
                .company(loggedUser.getCompany()).build();
        userRepository.save(user);
    }

}
