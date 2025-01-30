package com.mono.trigo.service;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.web.review.dto.CreateReviewResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private UserHelper userHelper;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ContentRepository contentRepository;

    private final User user = User.builder().id(1L).username("testUser123").build();
    private final Content content = Content.builder().id(1L).title("Test Content").build();

    private Review review;
    private ReviewRequest reviewRequest;

    @BeforeEach
    void setUp() {
        reviewRequest = ReviewRequest.builder()
                .rating(5)
                .reviewContent("좋은 관광지입니다!")
                .pictureList("pic1.jpg, pic2.jpg")
                .build();

        review = Review.of(reviewRequest, content, user);
        review.setId(1L);
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void createReview_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(contentRepository.findById(content.getId())).thenReturn(Optional.of(content));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // When
        CreateReviewResponse response = reviewService.createReview(1L, reviewRequest);

        // Then
        assertEquals(review.getId(), response.getReviewId());
        verify(reviewRepository, times(1)).save(argThat(review ->
                review.getUser().equals(user) &&
                review.getContent().equals(content) &&
                review.getRating().equals(reviewRequest.getRating()) &&
                review.getReviewContent().equals(reviewRequest.getReviewContent())
        ));
    }

    @Test
    @DisplayName("리뷰 생성 실패: 존재하지 않는 콘텐츠")
    void createReview_Fail_ContentNotFound() {
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.createReview(1L, reviewRequest));
        assertEquals(ApplicationError.CONTENT_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("리뷰 조회 성공")
    void getReviewByContentId_Success() {
        // Given
        when(reviewRepository.findByContentId(1L)).thenReturn(List.of(review));

        // When
        List<ReviewResponse> responses = reviewService.getReviewByContentId(1L);

        // Then
        assertEquals(1, responses.size());
        assertEquals(review.getId(), responses.get(0).getReviewId());
        assertEquals(review.getReviewContent(), responses.get(0).getReviewContent());
    }

    @Test
    @DisplayName("리뷰 조회 성공: 리뷰 없음")
    void getReviewByContentId_Success_Null() {
        // Given
        when(reviewRepository.findByContentId(1L)).thenReturn(List.of());

        // When
        List<ReviewResponse> responses = reviewService.getReviewByContentId(1L);

        // Then
        assertEquals(0, responses.size());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void updateReview_Success() {
        // Given
        reviewRequest.setRating(1);
        reviewRequest.setReviewContent("Updated review");

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When
        reviewService.updateReview(1L, reviewRequest);

        // Then
        verify(reviewRepository, times(1)).save(argThat(updatedReview ->
                updatedReview.getRating().equals(reviewRequest.getRating()) &&
                updatedReview.getReviewContent().equals(reviewRequest.getReviewContent())
        ));
    }

    @Test
    @DisplayName("리뷰 수정 실패: 존재하지 않는 리뷰")
    void updateReview_Fail_ReviewNotFound() {
        // Given
        when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.updateReview(2L, reviewRequest));
        assertEquals(ApplicationError.REVIEW_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("일정 수정 실패: 권한 없음")
    void updatePlan_Fail_UnauthorizedAccess() {
        // Given
        User otherUser = User.builder().id(2L).username("otherUser123").build();
        review.setUser(otherUser);

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.updateReview(1L, reviewRequest));
        assertEquals(ApplicationError.UNAUTHORIZED_ACCESS, exception.getError());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReview_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // When
        reviewService.deleteReview(1L);

        // Then
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
