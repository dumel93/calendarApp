package com.calendar.app.security;

import com.calendar.app.config.DataBaseHolder;
import com.calendar.app.entity.Company;
import com.calendar.app.service.CompanyServiceImpl;
import com.calendar.app.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CompanyServiceImpl masterTenantService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public final String TOKEN_PREFIX = "Bearer ";
    public final String HEADER_STRING = "Authorization";

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String tenantId;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            log.info("handle token");
            try {
                authToken = header.replace(TOKEN_PREFIX, "");
                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                    log.info("username " + username);
                    tenantId = jwtTokenUtil.getAudienceFromToken(authToken);
                    log.info("tenantId " + tenantId);
                    Company masterTenant = masterTenantService.findByTenantId(tenantId).orElse(null);
                    if (null == masterTenant) {
                        logger.error("An error during getting tenant name");
                        throw new BadCredentialsException("Invalid tenant and user.");
                    }
                    DataBaseHolder.setCurrentDb(masterTenant.getName());
                } catch (IllegalArgumentException ex) {
                    log.error("An error during getting username from token", ex);
                    resolver.resolveException(request, response, null, ex);
                    return;
                } catch (ExpiredJwtException ex) {
                    log.error("The token is expired", ex);
                    resolver.resolveException(request, response, null, ex);
                    return;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resolver.resolveException(request, response, null, e);
                return;
            }
        } else {
            log.error("Couldn't find bearer token");
        }

        if (username != null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
