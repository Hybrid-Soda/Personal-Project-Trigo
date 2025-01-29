package com.mono.trigo.jwt;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.domain.user.repository.UserRepository;
import com.mono.trigo.web.exception.advice.ApplicationException;
import com.mono.trigo.web.user.service.CustomUserDetailsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private final User user = User.builder().id(1L).username("username123").password("password123").build();

    @Test
    @DisplayName("유저 정보 생성 성공")
    void loadUserByUsername_Success() throws Exception {
        // Given
        String username = "username123";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertEquals("username123", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    @DisplayName("유저 정보 생성 실패: 존재하지 않는 유저")
    void loadUserByUsername_Fail_UserNotFound() {
        // Given
        String username = "unknown";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> customUserDetailsService.loadUserByUsername(username));

        assertEquals(ApplicationError.USER_IS_NOT_FOUND, exception.getError());
    }
}
