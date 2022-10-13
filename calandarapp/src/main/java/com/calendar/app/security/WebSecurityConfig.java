package com.calendar.app.security;

import com.calendar.app.util.BasicAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Configuration
    @Order(2)
    public static class FreeAccessEndpoints extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .requestMatchers()
                    .antMatchers(HttpMethod.POST, "/api/auth/tokens")
                    .antMatchers("/csrf",
                            "/v2/api-docs",
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/webjars/**")
                    .and()
                    .authorizeRequests()
                    .anyRequest()
                    .permitAll()
                    .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    @Order(1)
    public static class SecurityConfigurationForJwtProtectedEndpoints extends WebSecurityConfigurerAdapter {

        private final JwtUserDetailsService jwtUserDetailsService;

        public SecurityConfigurationForJwtProtectedEndpoints(JwtUserDetailsService jwtUserDetailsService) {
            this.jwtUserDetailsService = jwtUserDetailsService;
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Autowired
        public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public JWTAuthorizationFilter authenticationTokenFilterBean() throws Exception {
            return new JWTAuthorizationFilter(authenticationManager());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf()
                    .disable().cors().and().authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/meetings/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/meetings/**").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/locations/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/locations/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        }

    }


    @Configuration
    @Order(0)
    public static class SecurityConfigurationForBasicProtectedEndpoints extends WebSecurityConfigurerAdapter {

        @Value("${admin.basic.auth}")
        private String adminAuthWebServices;

        @Bean
        @Override
        protected UserDetailsService userDetailsService() {

            Pair<String, String> userAndPassword =
                    BasicAuthUtils.getUserAndPasswordFromBasic(adminAuthWebServices);

            UserDetails user = User
                    .withUsername(userAndPassword.getFirst())

                    .password(userAndPassword.getSecond())
                    .passwordEncoder(passwordEncoder()::encode)
                    .authorities("ROLE_ADMIN")
                    .build();

            return new InMemoryUserDetailsManager(user);
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.csrf()
                    .disable().cors().and().authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/admin/companies/**").permitAll()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}
