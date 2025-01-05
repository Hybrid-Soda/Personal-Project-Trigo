package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.repository.UserRepository;
import com.mono.trigo.web.user.dto.SignupRequest;

import com.mono.trigo.web.user.dto.SignupResponse;
import com.mono.trigo.web.user.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignupResponse signup(SignupRequest signupRequest) {

        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();
        String nickname = signupRequest.getNickname();

        Boolean isExistUsername = userRepository.existsByUsername(username);

//        if (isExistUsername) {
//            return;
//        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .birthday(signupRequest.getBirthday())
                .gender(signupRequest.getGender())
                .build();

        User savedUser = userRepository.save(user);

        return SignupResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .build();
    }

}
