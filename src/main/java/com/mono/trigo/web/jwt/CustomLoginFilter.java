package com.mono.trigo.web.jwt;

import com.mono.trigo.domain.user.entity.Refresh;
import com.mono.trigo.domain.user.repository.RefreshRepository;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;
import java.util.Iterator;
import java.util.Collection;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    public CustomLoginFilter(AuthenticationManager authenticationManager, RefreshRepository refreshRepository, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.refreshRepository = refreshRepository;
        this.jwtUtil = jwtUtil;
        // 로그인 경로 변경
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // POST 요청만 허용
        if (!request.getMethod().equals("POST")) {
            throw new ApplicationException(ApplicationError.INVALID_REQUEST_METHOD);
        }

        // 클라이언트 요청에서 username, password 추출
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        username = username != null ? username.trim() : "";
        password = password != null ? password : "";

        // username, password 검증하기 위해 authRequest 생성
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        // token 정보 검증을 위한 AuthenticationManager 전달
        return authenticationManager.authenticate(authRequest);
    }

    public void getSuccessHandler(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        this.successfulAuthentication(request, response, chain, authentication);
    }

    // 인증 성공시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        // 인증된 사용자의 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        // JWT 유틸리티를 사용하여 JWT 토큰을 생성 - username, role, expiredMs
        String username = authentication.getName();
        String role = auth.getAuthority();
        String access = jwtUtil.createJwt("access", username, role, 60*10*1000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 60*60*24*1000L);

        // refreshToken 저장
        addRefreshEntity(username, refresh, 60*60*24*1000L);

        // accessToken > header / refreshToken > cookie
        response.setHeader("access", access);
        response.addCookie(createCookie(refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    // 인증 실패시 실행하는 에소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        // 401 응답 코드 반환
        throw new ApplicationException(ApplicationError.AUTHENTICATION_FAILED);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(60*60*24);
        cookie.setHttpOnly(true);

        return cookie;
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
