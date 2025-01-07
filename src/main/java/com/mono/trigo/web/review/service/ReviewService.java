package com.mono.trigo.web.review.service;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.repository.ReviewRepository;

import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserHelper userHelper;

    public ReviewService(ReviewRepository reviewRepository, UserHelper userHelper) {
        this.reviewRepository = reviewRepository;
        this.userHelper = userHelper;
    }

    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        Review review = Review.builder()
//                .content(contentId)
                .user(userHelper.getCurrentUser())
                .rating(reviewRequest.getRating())
                .reviewContent(reviewRequest.getReviewContent())
                .pictureList(reviewRequest.getPictureList())
                .build();

        Review savedReview = reviewRepository.save(review);

        return CreateReviewResponse.builder()
                .reviewId(savedReview.getId())
                .build();
    }

}
