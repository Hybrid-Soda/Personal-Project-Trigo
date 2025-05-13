package com.mono.trigo.web.plan.dto;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.web.content.dto.ContentAreaDetailResponse;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "plan", timeToLive = 60*60*2)
public class PlanResponse {

    @Id private Long planId; // 일정 ID
    private Long userId; // 유저 ID
    private ContentAreaDetailResponse contentAreaDetailResponse; // 지역 ID
    private List<Long> contentIds; // 여행지 목록
    private String title; // 일정 제목
    private String description; // 일정 설명
    private Boolean isPublic; // 공개 여부
    private LocalDate startDate; // YYYY-MM-DD 형식
    private LocalDate endDate;   // YYYY-MM-DD 형식
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static PlanResponse of(Plan plan) {
        List<Long> contentIds = getContentIds(plan.getContents());

        return builder()
                .planId(plan.getId())
                .userId(plan.getUser().getId())
                .contentAreaDetailResponse(ContentAreaDetailResponse.of(plan.getAreaDetail()))
                .contentIds(contentIds)
                .title(plan.getTitle())
                .description(plan.getDescription())
                .isPublic(plan.getIsPublic())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .createdDate(plan.getCreatedDate())
                .modifiedDate(plan.getModifiedDate())
                .build();
    }

    private static List<Long> getContentIds(List<Content> contents) {
        return contents.stream()
                .map(Content::getId)
                .collect(Collectors.toList());
    }
}
