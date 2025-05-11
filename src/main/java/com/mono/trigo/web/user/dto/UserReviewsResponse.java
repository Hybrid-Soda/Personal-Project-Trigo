package com.mono.trigo.web.user.dto;

import com.mono.trigo.web.review.dto.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewsResponse {

    private List<ReviewResponse> reviewResponseList = new ArrayList<>();
}
