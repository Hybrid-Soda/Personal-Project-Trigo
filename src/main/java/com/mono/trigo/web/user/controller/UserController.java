package com.mono.trigo.web.user.controller;

import com.mono.trigo.web.exception.advice.ApplicationException;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.user.dto.TokenResponse;
import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.service.UserService;
import com.mono.trigo.web.user.service.ReissueService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ReissueService reissueService;

    public UserController(UserService userService, ReissueService reissueService) {
        this.userService = userService;
        this.reissueService = reissueService;
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(final @Valid @RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.status(201).body("User created successfully");
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.status(200).body(response);
    }

    // 회원 정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, final @Valid @RequestBody UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return ResponseEntity.status(200).body("User updated successfully");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 작성한 리뷰 조회
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewResponse> response = userService.getReviewsByUserId(userId);
        return ResponseEntity.status(200).body(response);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueService.reissue(request, response);
        return ResponseEntity.status(200).body("Access token updated successfully");
    }

    // 관리자
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.status(200).body("admin controller");
    }
}
