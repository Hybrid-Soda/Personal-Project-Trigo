package com.mono.trigo.web.plan.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class CreatePlanResponse {

    private Long planId;

    public static CreatePlanResponse of(Long planId) {
        return builder()
                .planId(planId)
                .build();
    }
}
