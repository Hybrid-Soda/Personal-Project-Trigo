package com.mono.trigo.domain.user.repository;

import com.mono.trigo.domain.user.entity.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
    Optional<User> findByUsername(String username);
}
