package com.mono.trigo.web.plan.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private Long planId;
    private Long areaDetailId; // 지역 ID
    private String detail; // 여행지 ID 목록
    private String title; // 일정 제목
    private String description; // 일정 설명
    private LocalDateTime startDate; // YYYY-MM-DD 형식
    private LocalDateTime endDate;   // YYYY-MM-DD 형식

}
