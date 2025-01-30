package com.mono.trigo.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.user.repository.UserRepository;

import com.mono.trigo.web.user.dto.CustomUserDetails;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserHelperTest {

    @InjectMocks
    private UserHelper userHelper;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private final User user = User.builder()
            .id(1L).username("user123").password("qwer123").role("member").build();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("사용자 존재 검증 성공")
    void validateUserExists_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> userHelper.validateUserExists(1L));
    }

    @Test
    @DisplayName("사용자 존재 검증 실패: 존재하지 않는 사용자")
    void validateUserExists_Failure() {
        // Given
        when(userRepository.existsById(2L)).thenReturn(false);

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> userHelper.validateUserExists(2L));
        assertEquals(ApplicationError.USER_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("인증된 유저 조회 성공")
    void getAuthenticatedUser_Success() {
        // Given
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(customUserDetails.getUsername()).thenReturn(user.getUsername());

        // When
        UserDetails result = userHelper.getAuthenticatedUser();

        // Then
        assertNotNull(result);
        assertEquals("user123", result.getUsername());
    }

    @Test
    @DisplayName("인증된 유저 조회 실패: 인증되지 않은 경우")
    void getAuthenticatedUser_Failure() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, userHelper::getAuthenticatedUser);
        assertEquals(ApplicationError.UNAUTHORIZED_ACCESS, exception.getError());
    }

    @Test
    @DisplayName("현재 로그인한 사용자 조회 성공")
    void getCurrentUser_Success() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);

        when(customUserDetails.getUsername()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        User result = userHelper.getCurrentUser();

        // Then
        assertNotNull(result);
        assertEquals("user123", result.getUsername());
    }
}