package com.mono.trigo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                // 경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers( "/**").permitAll()
                        .requestMatchers( "/h2-console/**").permitAll()
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers( "/api/v1/users/**").permitAll()
                        .requestMatchers( "/api/v1/plans/**").permitAll()
                        .requestMatchers( "/api/v1/contents/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // session disable
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
