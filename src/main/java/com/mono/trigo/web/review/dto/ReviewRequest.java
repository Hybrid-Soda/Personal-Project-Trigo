package com.mono.trigo.web.review.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewRequest {

    @NotNull(message = "점수는 필수 값입니다.")
    @Size(min = 1, max = 5, message = "점수는 1이상 5이하여야 합니다.")
    private Integer rating;

    @Size(max = 500, message = "리뷰는 500자 이하여야 합니다.")
    private String reviewContent;

    private String pictureList;
}
