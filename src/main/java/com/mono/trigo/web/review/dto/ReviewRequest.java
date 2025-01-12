package com.mono.trigo.web.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewRequest {

    private Long contentId;
    private Integer rating;
    private String reviewContent;
    private String pictureList;
}
