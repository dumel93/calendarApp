package com.calendar.app.service;


import com.calendar.app.config.DataBaseHolder;
import com.calendar.app.dto.AuthRequest;
import com.calendar.app.dto.AuthResponse;
import com.calendar.app.entity.Company;
import com.calendar.app.entity.User;
import com.calendar.app.repository.UserRepository;
import com.calendar.app.security.TenantInformation;
import com.calendar.app.util.AppStringUtils;
import com.calendar.app.util.BasicAuthUtils;
import com.calendar.app.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private Map<String, String> mapValue = new HashMap<>();
    private Map<String, String> userDbMap = new HashMap<>();

    private final JwtTokenUtil jwtTokenUtil;
    private final CompanyService masterTenantService;
    private final TenantService tenantService;
    private final UserRepository userRepository;
    private AuthenticationManager authManager;

    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil, CompanyService masterTenantService, TenantService tenantService,
                           UserRepository userRepository, AuthenticationManager authManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.masterTenantService = masterTenantService;
        this.tenantService = tenantService;
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    public AuthResponse generateToken(AuthRequest request) {

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                    UsernamePasswordAuthenticationToken(
                    request.getUserName(),
                    request.getPassword());
            final Authentication authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
            log.info("authentication {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(request.getUserName(), String.valueOf(request.getCompanyId()));
            handleTenant(request);
            return AuthResponse.builder().token(token).build();
        } catch (EntityNotFoundException e) {
            log.error(AppStringUtils.exceptionToString(e));
            throw e;
        } catch (BadCredentialsException e) {
            log.error(AppStringUtils.exceptionToString(e));
            throw new BadCredentialsException("Incorrect credentials");
        } catch (Exception e) {
            log.error(AppStringUtils.exceptionToString(e));
            throw new UsernameNotFoundException("User not found exception");
        }
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new BadCredentialsException("Authorization error. Or empty barear token or bad credentials.");
        String userName = BasicAuthUtils.getUserName(authentication);
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
    }

    private void handleTenant(AuthRequest request) {
        Company tenant = masterTenantService.findByTenantId(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Please contact service provider. Probably wrong companyId."));
        log.info("tenant {}", tenant);
        loadCurrentDatabaseInstance(tenant.getName(), tenant.getCompanyId().toString());
        setMetaDataAfterLogin();
    }

//    public boolean isLoggedAdmin(Authentication authentication) {
//        return CollectionUtils.isNotEmpty(authentication.getAuthorities()) &&
//                authentication.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .anyMatch(Roles.ADMIN.getRoleName()::equals);
//    }

    private void loadCurrentDatabaseInstance(String databaseName, String userName) {
        DataBaseHolder.setCurrentDb(databaseName);
        mapValue.put(userName, databaseName);
    }

    @ApplicationScope
    public void setMetaDataAfterLogin() {
        TenantInformation tenantInformation = new TenantInformation();
        if (mapValue.size() > 0) {
            mapValue.keySet().forEach(key -> {
                if (null == userDbMap.get(key)) {
                    userDbMap.putAll(mapValue);
                } else {
                    userDbMap.put(key, mapValue.get(key));
                }
            });
            mapValue = new HashMap<>();
        }
        tenantInformation.setMap(userDbMap);
    }
}
