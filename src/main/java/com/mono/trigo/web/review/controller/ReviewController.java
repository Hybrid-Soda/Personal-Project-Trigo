package com.mono.trigo.web.review.controller;

import com.mono.trigo.web.review.dto.CreateReviewResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.service.ReviewService;
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

    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(
            @PathVariable Long contentId, @RequestBody ReviewRequest reviewRequest) {
        CreateReviewResponse response = reviewService.createReview(contentId, reviewRequest);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewByContentId(@PathVariable Long contentId) {
        List<ReviewResponse> response = reviewService.getReviewByContentId(contentId);
        return ResponseEntity.status(200).body(response);
    }
}
