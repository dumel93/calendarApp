package com.calendar.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAuthProvider implements AuthenticationProvider {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String providedUsername = authentication.getPrincipal().toString();
        UserDetails user = jwtUserDetailsService.loadUserByUsername(providedUsername);
        log.info("User Details from UserService based on username-" + providedUsername + " : " + user);

        String providedPassword = authentication.getCredentials().toString();
        String correctPassword = user.getPassword();

        if(!providedPassword.equals(correctPassword))
            throw new BadCredentialsException("Incorrect Credentials");
        return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}