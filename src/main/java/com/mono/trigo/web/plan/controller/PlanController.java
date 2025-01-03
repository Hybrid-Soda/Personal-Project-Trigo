package com.mono.trigo.web.plan.controller;

import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;

import com.mono.trigo.web.plan.dto.PlanResponse;
import com.mono.trigo.web.plan.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlanController {

    private final PlanService planService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlan(@RequestBody PlanRequest planRequest) {
        CreatePlanResponse response = planService.createPlan(1, planRequest);
        return ResponseEntity.status(201).body(response);

    }

    // 전체 일정 조회
    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        List<PlanResponse> response = planService.getAllPlans();
        return ResponseEntity.status(200).body(response);
    }

    // 특정 일정 조회
    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long planId) {
        PlanResponse response = planService.getPlanById(planId);
        return ResponseEntity.status(200).body(response);
    }

    // 일정 수정
    @PutMapping("/{planId}")
    public ResponseEntity<String> updatePlan(@PathVariable Long planId, @RequestBody PlanRequest planRequest) {
        // TODO: Add service logic to update a plan
        return ResponseEntity.ok("Plan updated successfully");
    }

    // 일정 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable Long planId) {
        // TODO: Add service logic to delete a plan
        return ResponseEntity.ok("Plan deleted successfully");
    }

}
