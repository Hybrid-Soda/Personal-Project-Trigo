package com.mono.trigo.jwt;

import com.mono.trigo.domain.user.entity.Refresh;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.entity.Gender;
import com.mono.trigo.domain.user.repository.RefreshRepository;

import com.mono.trigo.web.jwt.JWTUtil;
import com.mono.trigo.web.jwt.CustomLoginFilter;
import com.mono.trigo.web.user.dto.CustomUserDetails;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDate;
import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomLoginFilterTest {

    @InjectMocks
    @VisibleForTesting
    private CustomLoginFilter customLoginFilter;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RefreshRepository refreshRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    private final User user = new User(1L, "username123", "password123", "nickname123",
            LocalDate.of(2000, 1, 1), Gender.MALE, "member");

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("인증 성공")
    void authentication_Success() {
        // Given
        request.setMethod("POST");
        request.setParameter("username", "username123");
        request.setParameter("password", "password123");

        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated("username123", "password123");
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), user.getPassword(), null);

        when(authenticationManager.authenticate(authRequest)).thenReturn(authentication);

        // When
        Authentication result = customLoginFilter.attemptAuthentication(request, response);

        // Then
        assertNotNull(result);
        assertEquals("username123", result.getName());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    @Test
    @DisplayName("인증 실패: 잘못된 메서드")
    void authentication_Fail_InvalidMethod() {
        // Given
        request.setMethod("PUT");
        request.setParameter("username", "username123");
        request.setParameter("password", "password123");

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> customLoginFilter.attemptAuthentication(request, response));
        assertEquals(ApplicationError.INVALID_REQUEST_METHOD, exception.getError());
    }

    @Test
    @DisplayName("JWT 인증 성공 - 액세스 토큰 및 리프레시 토큰 발급")
    void successfulAuthentication_Success() {
        // Given
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), user.getPassword(), authorities);

        String accessToken = "mockedAccessToken";
        String refreshToken = "mockedRefreshToken";

        when(jwtUtil.createJwt("access", user.getUsername(), user.getRole(), 60*10*1000L)).thenReturn(accessToken);
        when(jwtUtil.createJwt("refresh", user.getUsername(), user.getRole(), 60*60*24*1000L)).thenReturn(refreshToken);

        when(refreshRepository.save(any(Refresh.class))).thenReturn(null);

        // When
        customLoginFilter.getSuccessHandler(mock(HttpServletRequest.class), response, mock(FilterChain.class), authentication);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(accessToken, response.getHeader("access"));

        // 쿠키 확인
        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.length);
        assertEquals("refresh", cookies[0].getName());
        assertEquals(refreshToken, cookies[0].getValue());
    }
}
