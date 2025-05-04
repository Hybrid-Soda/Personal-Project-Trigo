package com.mono.trigo.web.review.dto;

import com.mono.trigo.domain.user.entity.User;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
