package com.mono.trigo.domain.user.impl;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.repository.UserRepository;

import com.mono.trigo.web.exception.advice.ApplicationException;
import com.mono.trigo.web.exception.entity.ApplicationError;
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
            throw new ApplicationException(ApplicationError.USER_IS_NOT_FOUND);
        }
    }

    public UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ApplicationException(ApplicationError.AUTHENTICATION_ERROR);
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public Long getCurrentUserId() {
        return Long.parseLong(getAuthenticatedUser().getUsername());
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(getAuthenticatedUser().getUsername());
    }
}
