package com.mono.trigo.web.review.controller;

import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable @Min(1) Long contentId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {

        CreateReviewResponse response = reviewService.createReview(contentId, reviewRequest);
        return ResponseEntity.status(201).body(response);
    }

    // 콘텐츠 별 리뷰 조회
    @GetMapping
    public ResponseEntity<ReviewListResponse> getReviewByContentId(
            @PathVariable @Min(1) Long contentId
    ) {

        ReviewListResponse response = reviewService.getReviewByContentId(contentId);
        return ResponseEntity.status(200).body(response);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable @Min(1) Long contentId,
            @PathVariable @Min(1) Long reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {

        reviewService.updateReview(contentId, reviewId, reviewRequest);
        return ResponseEntity.status(200).body("Review updated successfully");
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable @Min(1) Long contentId,
            @PathVariable @Min(1) Long reviewId
    ) {

        reviewService.deleteReview(contentId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
