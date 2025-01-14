package com.mono.trigo.web.review.dto;

import com.mono.trigo.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewUserResponse {

    private Long userId;
    private String username;
    private String nickname;

    public static ReviewUserResponse of(User user) {
        return builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
