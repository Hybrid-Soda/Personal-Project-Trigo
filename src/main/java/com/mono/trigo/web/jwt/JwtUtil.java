package com.mono.trigo.web.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // JWT 서명 및 검증을 위한 SecretKey
    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {

        // 주어진 비밀 키를 사용하여 HMAC SHA-256 알고리즘으로 SecretKey 생성
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT 토큰에서 사용자 이름(username) 추출하는 메서드
    public String getUsername(String token) {

        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    // JWT 토큰에서 역할(role) 추출하는 메서드
    public String getRole(String token) {

        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // JWT 토큰의 만료 여부를 확인하는 메서드
    public Boolean isExpired(String token) {

        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // JWT 토큰 생성 메서드 - username, role, 생성일, 만료일 정보를 저장
    public String createJwt(String username, String role, Long expiredMs) {
        
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}