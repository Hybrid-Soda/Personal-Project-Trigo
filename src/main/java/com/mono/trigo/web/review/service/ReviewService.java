package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.web.review.dto.CreateReviewResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public CreateReviewResponse createReview(UserDetails userDetails, ReviewRequest reviewRequest) {

        Review review = Review.builder()
//                .content(contentId)
                .user(userDetails.getUsername())
                .rating(reviewRequest.getRating())
                .reviewContent(reviewRequest.getReviewContent())
                .pictureList(reviewRequest.getPictureList())
                .build();
        return null;
    }

}
