package com.mono.trigo.domain.review.repository;

import com.mono.trigo.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);
    List<Review> findByContentId(Long contentId);
}
