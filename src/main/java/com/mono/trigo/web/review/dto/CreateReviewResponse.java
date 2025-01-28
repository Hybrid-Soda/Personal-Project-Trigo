package com.mono.trigo.web.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateReviewResponse {

    private Long reviewId;

    public static CreateReviewResponse of(Long reviewId) {
        return builder()
                .reviewId(reviewId)
                .build();
    }
}
