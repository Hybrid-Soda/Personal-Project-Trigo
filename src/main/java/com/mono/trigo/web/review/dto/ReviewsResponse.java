package com.mono.trigo.web.review.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "reviewContent", timeToLive = 60*60*3)
public class ReviewsResponse implements Serializable {

    @Id @JsonIgnore private Long contentId;
    private List<ReviewResponse> reviews = new ArrayList<>();
}
