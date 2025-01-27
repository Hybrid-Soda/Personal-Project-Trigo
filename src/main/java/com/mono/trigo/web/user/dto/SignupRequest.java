package com.mono.trigo.web.user.dto;

import com.mono.trigo.domain.user.entity.Gender;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Username은 필수 값입니다.")
    @Size(min = 5, max = 20, message = "Username은 5자 이상 20자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "Password는 필수 값입니다.")
    @Size(min = 6, max = 100, message = "Password는 최소 6자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,100}$",
            message = "Password는 문자와 숫자를 조합하여 6자 이상 입력해야 합니다."
    )
    private String password;

    @NotBlank(message = "Nickname은 필수 값입니다.")
    @Size(min = 2, max = 20, message = "Nickename은 최소 2자 이상 최대 20자 이하여야 합니다.")
    private String nickname;

    @Past(message = "Birthday는 과거 날짜여야 합니다.")
    private LocalDate birthday;

    @NotNull(message = "Gender는 필수 값입니다.")
    private Gender gender;

    private String role;
}
