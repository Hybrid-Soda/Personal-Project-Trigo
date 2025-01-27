package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final UserHelper userHelper;

    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));

        User user = userHelper.getCurrentUser();
        if (user == null) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        Review review = Review.builder()
                .content(content)
                .user(user)
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

        if (contentId == null || contentId <= 0) {
            throw new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND);
        }

        List<Review> reviews = reviewRepository.findByContentId(contentId);
        if (reviews.isEmpty()) {
            throw new ApplicationException(ApplicationError.REVIEW_IS_NOT_FOUND);
        }

        return reviews.stream()
                .map(ReviewResponse::of)
                .collect(Collectors.toList());
    }

    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {

        if (reviewId == null || reviewId <= 0) {
            throw new ApplicationException(ApplicationError.REVIEW_ID_IS_INVALID);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.REVIEW_IS_NOT_FOUND));

        User currentUser = userHelper.getCurrentUser();
        if (!review.getUser().equals(currentUser)) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        review.setRating(reviewRequest.getRating());
        review.setReviewContent(reviewRequest.getReviewContent());
        review.setPictureList(reviewRequest.getPictureList());
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.REVIEW_IS_NOT_FOUND));

        User currentUser = userHelper.getCurrentUser();
        if (!review.getUser().equals(currentUser)) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        reviewRepository.deleteById(reviewId);
    }
}
