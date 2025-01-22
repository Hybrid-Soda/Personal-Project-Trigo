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
    @DisplayName("회원가입 정상 작동 테스트")
    public void signup_Success() throws Exception {
        // Given
        SignupRequest signupRequest = SignupRequest.builder()
                .username("testUser")
                .password("testPassword")
                .nickname("testNickname")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .build();

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // When
        userService.signup(signupRequest);

        // Then
        verify(userRepository, times(1)).existsByUsername(signupRequest.getUsername());
        verify(bCryptPasswordEncoder, times(1)).encode(signupRequest.getPassword());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals(signupRequest.getUsername()) &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getNickname().equals(signupRequest.getNickname()) &&
                        user.getBirthday().equals(signupRequest.getBirthday()) &&
                        user.getGender() == signupRequest.getGender()
        ));
    }
}
