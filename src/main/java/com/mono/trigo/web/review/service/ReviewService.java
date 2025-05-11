package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mono.trigo.domain.review.entity.QReview.review;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final UserHelper userHelper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        Content content = getContent(contentId);
        User user = userHelper.getCurrentUser();
        if (user == null) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        // 리뷰 생성
        Review review = Review.of(reviewRequest, content, user);
        Review savedReview = reviewRepository.save(review);

        // 컨텐츠 리뷰 카운트
        content.getContentCount().plusReview(review.getRating());
        contentRepository.save(content);

        // 리뷰 Id 반환
        return CreateReviewResponse.of(savedReview.getId());
    }

    public ReviewListResponse getReviewByContentId(Long contentId) {

        String redisKey = "contentReviews::" + contentId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            return (ReviewListResponse) redisTemplate.opsForValue().get(redisKey);
        }

        List<Review> reviews = reviewRepository.findByContentId(contentId);
        ReviewListResponse reviewListResponse = new ReviewListResponse(reviews
                .stream()
                .map(ReviewResponse::of)
                .collect(Collectors.toList()));

        redisTemplate.opsForValue().set(redisKey, reviewListResponse, 2, TimeUnit.HOURS);
        return reviewListResponse;
    }

    @Transactional
    public void updateReview(Long contentId, Long reviewId, ReviewRequest reviewRequest) {

        checkReviewId(reviewId);
        Content content = getContent(contentId);
        Review review = getReview(reviewId);
        checkUser(review);

        // 리뷰 업데이트
        review.updateReview(reviewRequest);
        reviewRepository.save(review);

        // 컨텐츠 별점 업데이트
        content.getContentCount().setAverageScore(reviewRequest.getRating());
        contentRepository.save(content);

        // 캐시 제거
        deleteCache(contentId);
    }

    @Transactional
    public void deleteReview(Long contentId, Long reviewId) {

        checkReviewId(reviewId);
        Content content = getContent(contentId);
        Review review = getReview(reviewId);
        checkUser(review);

        // 리뷰 삭제
        reviewRepository.deleteById(reviewId);
        deleteCache(contentId);

        // 컨텐츠 리뷰 카운트
        content.getContentCount().minusReview(review.getRating());
        contentRepository.save(content);
    }

    private void checkReviewId(Long reviewId) {
        if (reviewId == null || reviewId <= 0) {
            throw new ApplicationException(ApplicationError.REVIEW_ID_IS_INVALID);
        }
    }

    private void checkUser(Review review) {
        User currentUser = userHelper.getCurrentUser();
        if (!review.getUser().equals(currentUser)) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }
    }

    private Content getContent(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.REVIEW_IS_NOT_FOUND));
    }

    private void deleteCache(Long contentId) {
        String redisKey = "contentReviews::" + contentId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            redisTemplate.opsForValue().getAndDelete(redisKey);
        }
    }
}
