package com.mono.trigo.web.review.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.review.repository.ReviewRedisRepository;
import com.mono.trigo.domain.content.repository.ContentRedisRepository;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewsResponse;
import com.mono.trigo.web.review.dto.CreateReviewResponse;

import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {

    private final UserHelper userHelper;
    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final ReviewRedisRepository reviewRedisRepository;
    private final ContentRedisRepository contentRedisRepository;

    public ReviewService(
            UserHelper userHelper,
            ReviewRepository reviewRepository,
            ContentRepository contentRepository,
            ReviewRedisRepository reviewRedisRepository,
            ContentRedisRepository contentRedisRepository
    ) {
        this.userHelper = userHelper;
        this.reviewRepository = reviewRepository;
        this.contentRepository = contentRepository;
        this.reviewRedisRepository = reviewRedisRepository;
        this.contentRedisRepository = contentRedisRepository;
    }

    // 리뷰 생성
    @Transactional
    public CreateReviewResponse createReview(Long contentId, ReviewRequest reviewRequest) {

        User user = userHelper.getCurrentUser();
        Content content = getContent(contentId);

        // 리뷰 생성
        Review review = Review.of(reviewRequest, content, user);
        Review savedReview = reviewRepository.save(review);

        // 컨텐츠 리뷰 통계 수정
        content.getContentCount().plusReview(review.getRating());
        contentRepository.save(content);

        // 캐시 제거
        contentRedisRepository.deleteById(contentId);
        return CreateReviewResponse.of(savedReview.getId());
    }

    // 리뷰 리스트 조회
    public ReviewsResponse getReviewByContentId(Long contentId) {

        // 캐시 조회
        Optional<ReviewsResponse> cachedResponse = reviewRedisRepository.findById(contentId);
        if (cachedResponse.isPresent()) return cachedResponse.get();

        // DB 조회
        ReviewsResponse reviewsResponse = new ReviewsResponse(
                contentId, reviewRepository.findByContentId(contentId));

        // 캐시 저장
        reviewRedisRepository.save(reviewsResponse);
        return reviewsResponse;
    }

    // 리뷰 수정
    @Transactional
    public void updateReview(Long contentId, Long reviewId, ReviewRequest reviewRequest) {

        // 검증
        Review review = getReview(reviewId);
        checkUser(review.getUser());
        checkReview(contentId, review);

        // 컨텐츠 리뷰 통계 수정
        Content content = getContent(contentId);
        content.getContentCount().updateAverageScore(review.getRating(), reviewRequest.getRating());
        contentRepository.save(content);

        // 리뷰 수정
        review.updateReview(reviewRequest);
        reviewRepository.save(review);

        // 캐시 제거
        reviewRedisRepository.deleteById(contentId);
        contentRedisRepository.deleteById(contentId);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long contentId, Long reviewId) {

        // 검증
        Review review = getReview(reviewId);
        checkUser(review.getUser());
        checkReview(contentId, review);

        // 컨텐츠 리뷰 통계 수정
        Content content = getContent(contentId);
        content.getContentCount().minusReview(review.getRating());
        contentRepository.save(content);

        // 리뷰 삭제
        checkUser(review.getUser());
        reviewRepository.deleteById(reviewId);

        // 캐시 제거
        reviewRedisRepository.deleteById(contentId);
        contentRedisRepository.deleteById(contentId);
    }

    private void checkUser(User reviewUser) {
        User currentUser = userHelper.getCurrentUser();
        if (!reviewUser.equals(currentUser)) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }
    }

    private void checkReview(Long contentId, Review review) {
        if (!Objects.equals(contentId, review.getContent().getId())) {
            throw new ApplicationException(ApplicationError.REVIEW_IS_NOT_CHILD_OF_CONTENT);
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
