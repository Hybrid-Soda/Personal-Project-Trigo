package com.mono.trigo.domain.user.repository;

import com.mono.trigo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);
    User findByUsername(String username);
}
