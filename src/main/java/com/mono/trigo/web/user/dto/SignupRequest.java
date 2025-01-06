package com.mono.trigo.web.user.dto;

import com.mono.trigo.domain.user.entity.Gender;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String username;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
    private String role;

}
