package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.dto.ReviewUserResponse;
import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.domain.user.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void signup(SignupRequest signupRequest) {

        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();
        String nickname = signupRequest.getNickname();

        Boolean isExistUsername = userRepository.existsByUsername(username);

        if (isExistUsername) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .birthday(signupRequest.getBirthday())
                .gender(signupRequest.getGender())
                .build();

        userRepository.save(user);
    }

    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.of(user);
    }

    public void updateUser(Long userId, UserRequest userRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNickname(userRequest.getNickname());
        user.setBirthday(userRequest.getBirthday());
        user.setGender(userRequest.getGender());
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public List<ReviewResponse> getReviewsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(ReviewResponse::of)
                .collect(Collectors.toList());
    }
}
