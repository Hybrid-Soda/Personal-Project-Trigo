package com.mono.trigo.domain.user.impl;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    private final UserRepository userRepository;

    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("user is not exist");
        }
    }

    public UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("authentication is not exist");
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public Long getCurrentUserId() {
        return Long.parseLong(getAuthenticatedUser().getUsername());
    }

    public User getCurrentUser() {
        return userRepository.findById(Long.parseLong(getAuthenticatedUser().getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("authentication is not exist"));
    }
}
