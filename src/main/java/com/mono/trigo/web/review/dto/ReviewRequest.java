package com.mono.trigo.web.review.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    @NotNull(message = "점수는 필수 값입니다.")
    @Min(value = 1, message = "점수는 1이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5이하여야 합니다.")
    private Integer rating;

    @Size(max = 500, message = "리뷰는 500자 이하여야 합니다.")
    private String reviewContent;

    private String pictureList;
}
