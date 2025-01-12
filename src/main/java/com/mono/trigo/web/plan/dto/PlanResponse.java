package com.mono.trigo.web.plan.dto;

import com.mono.trigo.domain.area.entity.AreaDetail;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private Long planId; // 일정 ID
    private Long userId; // 유저 ID
    private AreaDetail areaDetail; // 지역 ID
    private String detail; // 여행지 ID 목록
    private String title; // 일정 제목
    private String description; // 일정 설명
    private LocalDate startDate; // YYYY-MM-DD 형식
    private LocalDate endDate;   // YYYY-MM-DD 형식

}
