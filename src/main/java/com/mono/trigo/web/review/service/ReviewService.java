package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;
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
    private final ContentRepository contentRepository;
    private final UserHelper userHelper;

    public ReviewService(ReviewRepository reviewRepository, ContentRepository contentRepository, UserHelper userHelper) {
        this.reviewRepository = reviewRepository;
        this.contentRepository = contentRepository;
        this.userHelper = userHelper;
    }

    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        Content content = contentRepository.getReferenceById(contentId);

        Review review = Review.builder()
                .content(content)
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
        List<Review> reviews = reviewRepository.findByContentId(contentId);

        return reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getId())
                        .contentId(review.getContent().getId())
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

        Content content = contentRepository.getReferenceById(reviewRequest.getContentId());

        review.setContent(content);
        review.setRating(reviewRequest.getRating());
        review.setReviewContent(reviewRequest.getReviewContent());
        review.setPictureList(reviewRequest.getPictureList());
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Plan not found");
        }
        reviewRepository.deleteById(reviewId);
    }
}
