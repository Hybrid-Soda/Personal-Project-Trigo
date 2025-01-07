package com.mono.trigo.web.review.dto;

import com.mono.trigo.domain.user.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUserResponse {

    private Long userId;
    private String username;
    private String nickname;

    public ReviewUserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
