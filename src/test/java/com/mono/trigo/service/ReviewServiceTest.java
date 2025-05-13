package com.mono.trigo.service;

import com.mono.trigo.domain.content.entity.ContentCount;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.review.repository.ReviewRedisRepository;
import com.mono.trigo.domain.content.repository.ContentRedisRepository;

import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.review.dto.ContentReviewsResponse;
import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.web.review.dto.CreateReviewResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock private UserHelper userHelper;
    @Mock private ReviewRepository reviewRepository;
    @Mock private ContentRepository contentRepository;
    @Mock private ReviewRedisRepository reviewRedisRepository;
    @Mock private ContentRedisRepository contentRedisRepository;

    private final Long userId = 1L;
    private final Long contentId = 1L;
    private final Long review1Id = 1L;
    private final Long review2Id = 2L;
    private final User user = User.builder()
            .id(userId).username("testUser123").password("password123").nickname("nickname123")
            .build();
    private  final ContentCount contentCount = new ContentCount(contentId, 2, 4.5f);
    private final Content content = Content.builder()
            .id(contentId).title("Test Content").contentCount(contentCount)
            .build();
    private final ReviewRequest reviewRequest = ReviewRequest.builder()
            .reviewContent("very good").pictureList("").rating(5)
            .build();
    private final Review review1 = Review.builder()
            .id(review1Id).user(user).content(content).rating(5).pictureList("").reviewContent("very good")
            .build();
    private final Review review2 = Review.builder()
            .id(review2Id).user(user).content(content).rating(4).pictureList("").reviewContent("good")
            .build();
    private final ReviewResponse response1 = ReviewResponse.of(review1);
    private final ReviewResponse response2 = ReviewResponse.of(review2);
    private final List<ReviewResponse> ReviewResponseList =  List.of(response1, response2);
    private final ContentReviewsResponse contentReviewsResponse = new ContentReviewsResponse(content.getId(), ReviewResponseList);

    @Test
    @DisplayName("리뷰 생성 성공")
    void createReview_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
        when(reviewRepository.save(any(Review.class))).thenReturn(review1);

        // When
        CreateReviewResponse response = reviewService.createReview(contentId, reviewRequest);

        // Then
        assertEquals(review1.getId(), response.getReviewId());
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
        when(contentRepository.findById(contentId)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.createReview(contentId, reviewRequest));
        assertEquals(ApplicationError.CONTENT_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("리뷰 조회 성공: 캐시 히트")
    void getReviewByContentId_CacheHit() {
        // Given
        when(reviewRedisRepository.findById(contentId)).thenReturn(Optional.of(contentReviewsResponse));

        // When
        ContentReviewsResponse result = reviewService.getReviewByContentId(contentId);

        // Then
        assertThat(result).isEqualTo(contentReviewsResponse);
        verify(reviewRedisRepository, times(1)).findById(contentId);
        verifyNoInteractions(reviewRepository);
    }

    @Test
    @DisplayName("리뷰 조회 성공: 캐시 미스 후 DB 조회")
    void getReviewByContentId_Success() {
        // Given
        when(reviewRedisRepository.findById(contentId)).thenReturn(Optional.empty());
        when(reviewRepository.findByContentId(contentId)).thenReturn(ReviewResponseList);

        // When
        ContentReviewsResponse responses = reviewService.getReviewByContentId(contentId);

        // Then
        assertEquals(2, responses.getReviews().size());
        assertEquals(review1.getId(), responses.getReviews().get(0).getReviewId());
        assertEquals(review1.getReviewContent(), responses.getReviews().get(0).getReviewContent());
    }

    @Test
    @DisplayName("리뷰 조회 성공: 존재하지 않는 리뷰")
    void getReviewByContentId_Success_Null() {
        // Given
        Long contentId = 2L;
        when(reviewRedisRepository.findById(contentId)).thenReturn(Optional.empty());
        when(reviewRepository.findByContentId(contentId)).thenReturn(List.of());

        // When
        ContentReviewsResponse responses = reviewService.getReviewByContentId(contentId);

        // Then
        assertEquals(0, responses.getReviews().size());
    }

    @Test
    @Transactional
    @DisplayName("리뷰 수정 성공")
    void updateReview_Success() {
        // Given
        when(reviewRepository.findById(review2Id)).thenReturn(Optional.of(review2));
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
        when(userHelper.getCurrentUser()).thenReturn(user);

        // When
        reviewService.updateReview(contentId, review2Id, reviewRequest);

        // Then
        ArgumentCaptor<Content> contentCaptor = ArgumentCaptor.forClass(Content.class);
        verify(contentRepository, times(1)).save(contentCaptor.capture());
        Content savedContent = contentCaptor.getValue();
        assertThat(savedContent.getContentCount().getAverageScore()).isEqualTo(5);

        verify(reviewRepository, times(1)).save(review2);

        verify(reviewRedisRepository, times(1)).deleteById(contentId);
        verify(contentRedisRepository, times(1)).deleteById(contentId);

        verify(userHelper, times(1)).getCurrentUser();
    }

    @Test
    @DisplayName("리뷰 수정 실패: 존재하지 않는 리뷰")
    void updateReview_Fail_ReviewNotFound() {
        // Given
        Long reviewId = 3L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.updateReview(contentId, reviewId, reviewRequest));
        assertEquals(ApplicationError.REVIEW_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("일정 수정 실패: 권한 없음")
    void updatePlan_Fail_UnauthorizedAccess() {
        // Given
        User otherUser = User.builder().id(2L).username("otherUser123").build();
        review2.setUser(otherUser);

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(review2Id)).thenReturn(Optional.of(review2));

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> reviewService.updateReview(contentId, review2Id, reviewRequest));
        assertEquals(ApplicationError.UNAUTHORIZED_ACCESS, exception.getError());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReview_Success() {
        // Given
        when(reviewRepository.findById(review1Id)).thenReturn(Optional.of(review1));
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
        when(userHelper.getCurrentUser()).thenReturn(user);

        // When
        reviewService.deleteReview(contentId, review1Id);

        // Then
        verify(reviewRepository, times(1)).deleteById(review1Id);

        verify(reviewRedisRepository, times(1)).deleteById(contentId);
        verify(contentRedisRepository, times(1)).deleteById(contentId);
    }
}
