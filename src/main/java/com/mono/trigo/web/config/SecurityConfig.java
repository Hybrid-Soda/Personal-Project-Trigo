package com.mono.trigo.web.config;

import com.mono.trigo.web.jwt.JWTUtil;
import com.mono.trigo.web.jwt.JWTAuthenticationFilter;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                        .requestMatchers( "/**").permitAll()
                        .requestMatchers( "/h2-console/**").permitAll()
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers( "/api/v1/users/**").permitAll()
                        .requestMatchers( "/api/v1/plans/**").permitAll()
                        .requestMatchers( "/api/v1/contents/**").permitAll()
                        .requestMatchers("/api/v1/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // authentication
                .addFilterAt(new JWTAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // session disable
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
