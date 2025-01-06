package com.mono.trigo.web.user.controller;

import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.service.UserService;
import com.mono.trigo.web.user.service.ReissueService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
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
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return ResponseEntity.status(200).body("User updated successfully");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(204).body("User deleted successfully");
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            // refreshToken 확인
            String refreshToken = reissueService.getRefreshToken(request);
            reissueService.validateRefreshToken(refreshToken);

            // accessToken 갱신
            String newAccessToken = reissueService.generateNewAccessToken(refreshToken);
            response.setHeader("access", newAccessToken);

            return ResponseEntity.status(200).body("Access token updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // 관리자
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.status(200).body("admin controller");
    }
}
