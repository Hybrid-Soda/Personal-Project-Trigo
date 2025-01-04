package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.repository.UserRepository;
import com.mono.trigo.web.user.dto.SignupRequest;

import com.mono.trigo.web.user.dto.SignupResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SignupResponse signup(SignupRequest signupRequest) {

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .birthday(signupRequest.getBirthday())
                .gender(signupRequest.getGender())
                .location(signupRequest.getLocation())
                .build();

        User savedUser = userRepository.save(user);

        return SignupResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

}
