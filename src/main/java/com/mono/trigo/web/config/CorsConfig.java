package com.mono.trigo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // CORS 설정을 관리할 소스를 생성
        CorsConfiguration config = new CorsConfiguration(); // CORS 설정 객체 생성

        config.setAllowCredentials(true); // 자격 증명 정보를 포함할 수 있도록 설정 (예: 쿠키, 인증 헤더)
        config.addAllowedOrigin(CorsConfiguration.ALL); // 모든 도메인에서 요청을 허용
        config.addAllowedHeader(CorsConfiguration.ALL); // 모든 헤더를 허용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 특정 HTTP 메서드를 허용

        source.registerCorsConfiguration("/**", config); // 특정 URL 패턴에 대해 CORS 설정 등록
        return new CorsFilter(source);
    }

}
