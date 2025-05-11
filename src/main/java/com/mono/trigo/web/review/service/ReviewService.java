package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.review.repository.ReviewRedisRepository;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {

    private final UserHelper userHelper;
    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final ReviewRedisRepository reviewRedisRepository;

    public ReviewService(
            UserHelper userHelper,
            ReviewRepository reviewRepository,
            ContentRepository contentRepository,
            ReviewRedisRepository reviewRedisRepository
    ) {
        this.userHelper = userHelper;
        this.reviewRepository = reviewRepository;
        this.contentRepository = contentRepository;
        this.reviewRedisRepository = reviewRedisRepository;
    }

    // 리뷰 생성
    @Transactional
    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        User user = userHelper.getCurrentUser();
        Content content = getContent(contentId);

        // 리뷰 생성
        Review review = Review.of(reviewRequest, content, user);
        Review savedReview = reviewRepository.save(review);

        // 컨텐츠 리뷰 카운트
        content.getContentCount().plusReview(review.getRating());
        contentRepository.save(content);

        // 리뷰 Id 반환
        return CreateReviewResponse.of(savedReview.getId());
    }

    // 리뷰 리스트 조회
    public ReviewListResponse getReviewByContentId(Long contentId) {

        // 캐시 조회
        Optional<ReviewListResponse> cachedResponse = reviewRedisRepository.findById(String.valueOf(contentId));
        if (cachedResponse.isPresent()) {
            return cachedResponse.get();
        }

        // DB 조회
        ReviewListResponse reviewListResponse =
                new ReviewListResponse(contentId, reviewRepository.findByContentId(contentId));

        // 캐시 저장
        reviewRedisRepository.save(reviewListResponse);
        return reviewListResponse;
    }

    // 리뷰 수정
    @Transactional
    public void updateReview(Long contentId, Long reviewId, ReviewRequest reviewRequest) {

        Review review = getReview(reviewId);
        checkUser(review.getUser());

        // 리뷰 수정
        review.updateReview(reviewRequest);
        reviewRepository.save(review);

        // 컨텐츠 별점 수정
        Content content = getContent(contentId);
        content.getContentCount().setAverageScore(reviewRequest.getRating());
        contentRepository.save(content);

        // 캐시 제거
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long contentId, Long reviewId) {

        // 리뷰 삭제
        Review review = getReview(reviewId);
        checkUser(review.getUser());
        reviewRepository.deleteById(reviewId);

        // 컨텐츠 리뷰 카운트
        Content content = getContent(contentId);
        content.getContentCount().minusReview(review.getRating());
        contentRepository.save(content);

        // 캐시 제거
    }

    private void checkUser(User reviewUser) {
        User currentUser = userHelper.getCurrentUser();
        if (!reviewUser.equals(currentUser)) {
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
}
