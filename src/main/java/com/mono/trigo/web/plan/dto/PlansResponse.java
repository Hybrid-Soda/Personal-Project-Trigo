package com.mono.trigo.web.plan.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlansResponse {

    private List<PlanResponse> plans = new ArrayList<>();
}
