package com.mono.trigo.service.user;

import com.mono.trigo.domain.user.entity.Gender;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.service.UserService;
import com.mono.trigo.domain.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("회원가입이 정상 작동하는지 테스트")
    void signup_Success() throws Exception {
        // Given
        SignupRequest request = SignupRequest.builder()
                .username("testUser")
                .password("testPassword")
                .nickname("testNickname")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        userService.signup(request);

        // Then
        verify(userRepository, times(1)).existsByUsername(request.getUsername());
        verify(bCryptPasswordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals(request.getUsername()) &&
                user.getNickname().equals(request.getNickname()) &&
                user.getBirthday().equals(request.getBirthday()) &&
                user.getPassword().equals("encodedPassword") &&
                user.getGender() == request.getGender()
        ));
    }

    @Test
    @DisplayName("username 이미 있는 경우 테스트")
    void signup_shouldThrowException_whenUsernameExists() throws Exception {
        // Given
        SignupRequest request = SignupRequest.builder()
                .username("existingUser")
                .password("existingPassword")
                .nickname("existingNickname")
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // When


        // Then

    }
}
