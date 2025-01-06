package com.mono.trigo.web.user.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.web.user.dto.CustomUserDetails;
import com.mono.trigo.domain.user.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 인증 시 호출하는 메서드 - 주어진 사용자 이름(username)을 기반으로 사용자 정보를 로드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        // 사용자가 존재하면 CustomUserDetails로 변환하여 반환
        if (user != null) {
            return new CustomUserDetails(user);
        }

        // 사용자가 존재하진 않으면 throw UsernameNotFoundException
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
