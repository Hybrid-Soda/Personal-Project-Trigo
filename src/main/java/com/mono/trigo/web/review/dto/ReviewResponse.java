package com.mono.trigo.web.review.dto;

import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.user.entity.User;
import lombok.*;

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

    public static ReviewResponse of(Review review) {
        return builder()
                .reviewId(review.getId())
                .contentId(review.getContent().getId())
                .rating(review.getRating())
                .reviewContent(review.getReviewContent())
                .pictureList(review.getPictureList())
                .reviewUserResponse(ReviewUserResponse.of(review.getUser()))
                .build();
    }
}
