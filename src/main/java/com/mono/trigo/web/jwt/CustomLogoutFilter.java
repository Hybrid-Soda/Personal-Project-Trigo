package com.mono.trigo.web.jwt;

import com.mono.trigo.domain.user.repository.RefreshRepository;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (!isLogoutRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null || !isValidRefreshToken(refreshToken, response)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        processLogout(refreshToken, response);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return "/api/v1/users/logout".equals(request.getRequestURI()) &&
                "POST".equalsIgnoreCase(request.getMethod());
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isValidRefreshToken(String refreshToken, HttpServletResponse response) {
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return false;
        }

        return "refresh".equals(jwtUtil.getCategory(refreshToken)) && refreshRepository.existsByRefresh(refreshToken);
    }

    private void processLogout(String refreshToken, HttpServletResponse response) {
        refreshRepository.deleteByRefresh(refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/api/v1/users");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
