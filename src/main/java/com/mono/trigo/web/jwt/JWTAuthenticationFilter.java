package com.mono.trigo.web.jwt;

import com.mono.trigo.web.user.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 로그인 경로 변경
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // username, password 검증하기 위해 token에 담음
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token 정보 검증을 위한 AuthenticationManager 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {

        // 사용자 인증 정보에서 CustomUserDetails 객체 추출
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 인증된 사용자의 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        // 사용자의 첫 번째 권한 정보를 추출
        GrantedAuthority auth = iterator.next();

        // JWT 유틸리티를 사용하여 JWT 토큰을 생성 - username, role, 만료 시간(60분 * 60초 * 24시간 * 7일 = 1주일)
        String username = customUserDetails.getUsername();
        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(username, role, 60*60*24*7L);

        // HTTP 응답 헤더에 생성된 JWT 토큰을 "Authorization" 헤더로 추가
        response.addHeader("Authorization", "Bearer " + token);
    }

    // 로그인 실패시 실행하는 에소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {

        // 401 응답 코드 반환
        response.setStatus(401);
    }
}
