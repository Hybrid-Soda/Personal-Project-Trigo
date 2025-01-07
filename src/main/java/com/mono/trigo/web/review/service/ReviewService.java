package com.mono.trigo.web.review.service;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.repository.ReviewRepository;

import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.dto.ReviewUserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ReviewResponse> getReviewByContentId(Long contentId) {
        List<Review> reviews = reviewRepository.findAll(); // 나중에 contentId 이용해서 가져오는걸로 변경

        return reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getId())
                        .rating(review.getRating())
                        .reviewContent(review.getReviewContent())
                        .pictureList(review.getPictureList())
                        .reviewUserResponse(new ReviewUserResponse(review.getUser()))
                        .build())
                .collect(Collectors.toList());
    }

    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(reviewRequest.getRating());
        review.setReviewContent(reviewRequest.getReviewContent());
        review.setPictureList(reviewRequest.getPictureList());
        reviewRepository.save(review);
    }
}
