package com.mono.trigo.web.review.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponse {

    @Builder.Default
    private List<ReviewResponse> reviewResponseList = new ArrayList<>();
}
