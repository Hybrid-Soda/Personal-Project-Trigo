package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.user.repository.UserRepository;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.review.repository.ReviewRepository;

import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHelper userHelper;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ApplicationException(ApplicationError.USERNAME_IS_EXISTED);
        }

        if (userRepository.existsByNickname(signupRequest.getNickname())) {
            throw new ApplicationException(ApplicationError.NICKNAME_IS_EXISTED);
        }

        User user = User.of(signupRequest, bCryptPasswordEncoder.encode(signupRequest.getPassword()));

        userRepository.save(user);
    }

    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.USER_IS_NOT_FOUND));

        return UserResponse.of(user);
    }

    public void updateUser(Long userId, UserRequest userRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.USER_IS_NOT_FOUND));

        if (!user.equals(userHelper.getCurrentUser())) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        user.update(userRequest);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.USER_IS_NOT_FOUND));

        if (!user.equals(userHelper.getCurrentUser())) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        userRepository.deleteById(userId);
    }

    public List<ReviewResponse> getReviewsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ApplicationException(ApplicationError.USER_IS_NOT_FOUND);
        }

        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(ReviewResponse::of)
                .collect(Collectors.toList());
    }
}
