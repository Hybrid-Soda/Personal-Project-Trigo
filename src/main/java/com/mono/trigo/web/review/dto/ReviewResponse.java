package com.mono.trigo.web.review.dto;

import com.mono.trigo.domain.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long reviewId;
    private Long contentId;
    private Integer rating;
    private String reviewContent;
    private String pictureList;
    private ReviewUserResponse reviewUserResponse;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static ReviewResponse of(Review review) {
        return builder()
                .reviewId(review.getId())
                .contentId(review.getContent().getId())
                .rating(review.getRating())
                .reviewContent(review.getReviewContent())
                .pictureList(review.getPictureList())
                .reviewUserResponse(ReviewUserResponse.of(review.getUser()))
                .createdDate(review.getCreatedDate())
                .modifiedDate(review.getModifiedDate())
                .build();
    }
}
