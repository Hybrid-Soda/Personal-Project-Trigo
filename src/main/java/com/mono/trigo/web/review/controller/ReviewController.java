package com.mono.trigo.web.review.controller;

import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/contents/{contentId}/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(
            @PathVariable Long contentId, @Valid @RequestBody ReviewRequest reviewRequest) {
        CreateReviewResponse response = reviewService.createReview(contentId, reviewRequest);
        return ResponseEntity.status(201).body(response);
    }

    // 콘텐츠 별 리뷰 조회
    @GetMapping
    public ResponseEntity<ReviewListResponse> getReviewByContentId(
            @PathVariable @NotNull(message = "contentId는 필수입니다.")
            @Min(value = 1, message = "contentId는 1 이상의 값이어야 합니다.")
            Long contentId) {
        ReviewListResponse response = reviewService.getReviewByContentId(contentId);
        return ResponseEntity.status(200).body(response);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId, @Valid @RequestBody ReviewRequest reviewRequest) {
        reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.status(200).body("Review updated successfully");
    }

    // 리뷰 삭제
    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
