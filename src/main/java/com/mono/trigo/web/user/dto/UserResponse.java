package com.mono.trigo.web.user.dto;

import com.mono.trigo.domain.user.entity.Gender;
import com.mono.trigo.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long userId;
    private String username;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;

    public static UserResponse of(User user) {
        return builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .build();
    }
}
