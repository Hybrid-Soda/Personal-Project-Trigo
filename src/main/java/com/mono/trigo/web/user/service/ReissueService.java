package com.mono.trigo.web.user.service;

import com.mono.trigo.web.jwt.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReissueService {

    private final JWTUtil jwtUtil;

    public ReissueService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        // HTTP 요청에서 쿠키를 통해 리프레시 토큰을 가져옴
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void validateRefreshToken(String token) {
        // 토큰 유무 확인
        if (token == null) {
            throw new IllegalArgumentException("refresh token null");
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("refresh token expired");
        }

        // 토큰 종류 확인
        String category = jwtUtil.getCategory(token);
        if (!"refresh".equals(category)) {
            throw new IllegalArgumentException("invalid refresh token");
        }
    }

    public String generateNewAccessToken(String refreshToken) {
        // 10분 동안 유효한 새 액세스 토큰 생성
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("access", username, role, 60*10*1000L);
    }
}
