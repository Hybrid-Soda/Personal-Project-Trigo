package com.mono.trigo.web.review.controller;

import com.mono.trigo.web.review.dto.CreateReviewResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/spots/{contentId}/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody ReviewRequest reviewRequest) {
        CreateReviewResponse response = reviewService.createReview(userDetails, reviewRequest);
        return ResponseEntity.status(201).body(response);
    }

}
