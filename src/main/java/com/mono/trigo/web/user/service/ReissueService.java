package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.user.entity.Refresh;
import com.mono.trigo.web.exception.advice.ApplicationException;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.jwt.JWTUtil;
import com.mono.trigo.web.user.dto.TokenResponse;
import com.mono.trigo.domain.user.repository.RefreshRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
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
            throw new ApplicationException(ApplicationError.ACCESS_TOKEN_NOT_FOUND);
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(ApplicationError.ACCESS_TOKEN_IS_INVALID);
        }

        // 토큰 종류 확인
        String category = jwtUtil.getCategory(token);
        if (!"refresh".equals(category)) {
            throw new ApplicationException(ApplicationError.ACCESS_TOKEN_IS_INVALID);
        }
    }

    public TokenResponse generateNewTokens(String refreshToken) {
        // 10분 동안 유효한 새 액세스 토큰 생성
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccess = jwtUtil.createJwt("access", username, role, 60*10*1000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 60*60*24*1000L);

        refreshRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(username, newRefresh, 60*60*24*1000L);

        return TokenResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }

    private void addRefreshEntity(String username, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refreshToken);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
