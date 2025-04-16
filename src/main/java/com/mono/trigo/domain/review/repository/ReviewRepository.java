package com.mono.trigo.domain.review.repository;

import com.mono.trigo.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    @Query("SELECT r FROM Reviews r JOIN FETCH r.user WHERE r.content.id = :contentId")
    List<Review> findByContentId(@Param("contentId") Long contentId);
}
