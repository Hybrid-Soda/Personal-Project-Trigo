package com.mono.trigo.web.plan.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanListResponse {

    @Builder.Default
    private List<PlanResponse> planResponseList = new ArrayList<>();

    public static PlanListResponse of(List<PlanResponse> planResponseList) {
        return builder()
                .planResponseList(planResponseList)
                .build();
    }
}
