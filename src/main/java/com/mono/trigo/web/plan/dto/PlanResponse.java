package com.mono.trigo.web.plan.dto;

import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private Long planId; // 일정 ID
    private Long userId; // 유저 ID
    private AreaDetail areaDetail; // 지역 ID
    private List<Content> contents; // 여행지 목록
    private String title; // 일정 제목
    private String description; // 일정 설명
    private LocalDate startDate; // YYYY-MM-DD 형식
    private LocalDate endDate;   // YYYY-MM-DD 형식

}
