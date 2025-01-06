package com.mono.trigo.web.jwt;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.web.user.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import io.jsonwebtoken.ExpiredJwtException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader("access");

        // 토큰 유무 확인
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 정상 확인
        if (!validateToken(accessToken, response)) {
            return;
        }

        setAuthentication(accessToken);
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token, HttpServletResponse response) throws IOException {

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "Access token expired");
            return false;
        }

        // 토큰 종류 확인
        String category = jwtUtil.getCategory(token);
        if (!"access".equals(category)) {
            sendErrorResponse(response, "Invalid access token");
            return false;
        }

        return true;
    }

    private void setAuthentication(String token) {
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // UserDetails 회원 정보 객체 담기
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성 후 세션에 사용자 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
