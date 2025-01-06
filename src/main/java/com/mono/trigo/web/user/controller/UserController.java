package com.mono.trigo.web.user.controller;

import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
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

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        SignupResponse response = userService.signup(signupRequest);
        return ResponseEntity.status(201).body(response);
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.status(200).body(response);
    }

    // 회원 정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return ResponseEntity.status(200).body("User updated successfully");
    }

    // 관리자
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.status(200).body("admin controller");
    }

}
