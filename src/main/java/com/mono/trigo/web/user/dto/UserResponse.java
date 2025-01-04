package com.mono.trigo.web.user.dto;

import com.mono.trigo.domain.user.entity.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long userId;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
    private String location;

}
