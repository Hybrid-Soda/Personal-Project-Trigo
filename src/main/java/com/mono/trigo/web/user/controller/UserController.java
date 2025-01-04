package com.mono.trigo.web.user.controller;

import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.dto.SignupResponse;
import com.mono.trigo.web.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        SignupResponse response = userService.signup(signupRequest);
        return ResponseEntity.status(201).body(response);
    }

}
