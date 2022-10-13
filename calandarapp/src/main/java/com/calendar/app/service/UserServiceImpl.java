package com.calendar.app.service;

import com.calendar.app.config.DataBaseHolder;
import com.calendar.app.dto.UserRequest;
import com.calendar.app.entity.User;
import com.calendar.app.repository.UserRepository;
import com.calendar.app.util.BasicAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder, UserRepository userRepository) {
        this.repository = repository;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public void createUser(HttpServletRequest request, UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication {}", authentication);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        User loggedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
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
        DataBaseHolder.setCurrentDb(loggedUser.getCompany().getName());
    }

}
