package com.mono.trigo.web.plan.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequest {

    @NotNull(message = "지역은 필수 값입니다.")
    private Long areaDetailId;

    private List<Long> contents;

    @NotBlank(message = "일정 제목은 필수 값입니다.")
    @Size(min = 2, max = 100, message = "일정 제목은 2자 이상 100자 이하여야 합니다.")
    private String title;

    @Size(max = 500, message = "500자 이하여야 합니다.")
    private String description;

    @NotNull(message = "시작 날짜는 필수 값입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜는 필수 값입니다.")
    private LocalDate endDate;

    @Builder.Default
    private Boolean isPublic = false;
}
