package com.mono.trigo.web.plan.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequest {

    private Long areaDetailId; // 지역 ID
    private String detail; // 여행지 ID 목록
    private String title; // 일정 제목
    private String description; // 일정 설명
    private LocalDate startDate; // YYYY-MM-DD 형식
    private LocalDate endDate;   // YYYY-MM-DD 형식

}
