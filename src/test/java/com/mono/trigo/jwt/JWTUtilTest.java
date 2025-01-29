package com.mono.trigo.jwt;

import com.mono.trigo.web.jwt.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;

    private String secretKey = "test-secret-key";

    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil(secretKey);
    }

//    @Test
//    @DisplayName("JWT 토큰 생성 및 검증")
//    void createAndValidateJwt() {
//        // Given
//        String username = "testUser";
//        String role = "ROLE_USER";
//        long expiration = 1000 * 60 * 10; // 10분
//
//        // When
//        String token = jwtUtil.createJwt("access", username, role, expiration);
//        Claims claims = jwtUtil.parseClaims(token);
//
//        // Then
//        assertEquals(username, claims.getSubject());
//        assertEquals(role, claims.get("role", String.class));
//        assertEquals("access", claims.get("category", String.class));
//    }
//
//    @Test
//    @DisplayName("만료된 JWT 토큰 검증")
//    void validateExpiredJwt() {
//        // Given
//        String expiredToken = jwtUtil.createJwt("access", "testUser", "ROLE_USER", -1000L); // 이미 만료됨
//
//        // When & Then
//        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isExpired(expiredToken));
//    }
//
//    @Test
//    @DisplayName("잘못된 JWT 토큰 검증")
//    void validateInvalidJwt() {
//        // Given
//        String invalidToken = "invalid.jwt.token";
//
//        // When & Then
//        assertThrows(SignatureException.class, () -> jwtUtil.parseClaims(invalidToken));
//    }
}