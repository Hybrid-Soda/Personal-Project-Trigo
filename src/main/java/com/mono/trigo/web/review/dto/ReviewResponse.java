package com.mono.trigo.web.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Long contentId;
    private Integer rating;
    private String reviewContent;
    private String pictureList;
    private ReviewUserResponse reviewUserResponse;
}
