package com.mono.trigo.web.plan.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanResponse {

    private String status;
    private String message;
    private Long planId;

}
