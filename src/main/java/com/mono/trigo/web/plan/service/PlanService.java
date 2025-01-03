package com.mono.trigo.web.plan.service;

import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.domain.plan.repository.PlanRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public CreatePlanResponse createPlan(Integer userId, PlanRequest planRequest) {
        validateRequest(planRequest);

        Plan plan = Plan.builder()
                .title(planRequest.getTitle())
                .description(planRequest.getDescription())
                .startDate(planRequest.getStartDate())
                .endDate(planRequest.getEndDate())
                .detail(planRequest.getDetail())
                .isPublic(false)
                .build();
//                .user(plan.getUser())
//                .areaDetail(plan.getAreaDetail())

        Plan savedPlan = planRepository.save(plan);

        return CreatePlanResponse.builder()
                .status("201")
                .message("Plan created successfully")
                .planId(savedPlan.getId())
                .build();
    }

    private void validateRequest(PlanRequest planRequest) {
        if (planRequest.getTitle() == null || planRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (planRequest.getDescription() == null || planRequest.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }
        if (planRequest.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (planRequest.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required.");
        }
        if (planRequest.getStartDate().isAfter(planRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
    }

}
