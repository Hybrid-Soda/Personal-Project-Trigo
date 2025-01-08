package com.mono.trigo.domain.like.repository;

import com.mono.trigo.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
