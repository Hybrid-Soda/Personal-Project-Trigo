package com.mono.trigo.web.config;

import com.mono.trigo.web.jwt.CustomLogoutFilter;
import com.mono.trigo.web.jwt.JWTFilter;
import com.mono.trigo.web.jwt.JWTUtil;
import com.mono.trigo.web.jwt.CustomLoginFilter;
import com.mono.trigo.domain.user.repository.RefreshRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/h2-console/**",
            "/swagger-ui/**",
            "/api/v1/",
            "/api/v1/users/signup",
            "/api/v1/users/login",
            "/api/v1/plans",
            "/api/v1/area/**",
            "/api/v1/contents/**"
    };

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, CorsConfig corsConfig, RefreshRepository refreshRepository) throws Exception {
        return http
                // csrf disable
                .csrf(AbstractHttpConfigurer::disable)
                // form login disable
                .formLogin(AbstractHttpConfigurer::disable)
                // http basic auth disable
                .httpBasic(AbstractHttpConfigurer::disable)
                // frame options disable for h2-console
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // authorization
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/api/v1/users/**").authenticated()
                        .requestMatchers("/api/v1/plans/**").authenticated()
                        .requestMatchers( "/api/v1/users/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 커스텀 logout filter 추가
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class)
                // authentication - JWTFilter를 JWTAuthenticationFilter 이전에 추가
                .addFilterBefore(new JWTFilter(jwtUtil), CustomLoginFilter.class)
                // authentication - JWTAuthenticationFilter를 UsernamePasswordAuthenticationFilter와 동일한 위치에 추가
                .addFilterAt(
                        new CustomLoginFilter(authenticationManager(authenticationConfiguration), refreshRepository, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                // cors filter
                .addFilter(corsConfig.corsFilter())
                // session disable
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
