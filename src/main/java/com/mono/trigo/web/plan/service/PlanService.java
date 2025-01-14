package com.mono.trigo.web.plan.service;

import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.PlanResponse;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.like.repository.LikeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final LikeRepository likeRepository;
    private final AreaDetailRepository areaDetailRepository;
    private final ContentRepository contentRepository;
    private final UserHelper userHelper;

    public PlanService(
            PlanRepository planRepository,
            LikeRepository likeRepository,
            AreaDetailRepository areaDetailRepository,
            ContentRepository contentRepository,
            UserHelper userHelper) {

        this.planRepository = planRepository;
        this.likeRepository = likeRepository;
        this.areaDetailRepository = areaDetailRepository;
        this.contentRepository = contentRepository;
        this.userHelper = userHelper;
    }

    public CreatePlanResponse createPlan(PlanRequest planRequest) {

        validateRequest(planRequest);
        User user = userHelper.getCurrentUser();
        AreaDetail areaDetail = areaDetailRepository.getReferenceById(planRequest.getAreaDetailId());
        List<Content> contents = new ArrayList<>();

        if (!planRequest.getContents().isEmpty()) {
            for (Long contentId : planRequest.getContents()) {
                Content content = contentRepository.findById(contentId)
                                .orElseThrow(() -> new RuntimeException("Content not found"));
                contents.add(content);
            }
        }

        Plan plan = Plan.builder()
                .user(user)
                .areaDetail(areaDetail)
                .title(planRequest.getTitle())
                .description(planRequest.getDescription())
                .startDate(planRequest.getStartDate())
                .endDate(planRequest.getEndDate())
                .contents(contents)
                .isPublic(false)
                .build();

        Plan savedPlan = planRepository.save(plan);

        return CreatePlanResponse.builder()
                .planId(savedPlan.getId())
                .build();
    }

    public List<PlanResponse> getAllPlans() {

        List<Plan> plans = planRepository.findAll();

        return plans.stream()
                .map(PlanResponse::of)
                .collect(Collectors.toList());
    }

    public PlanResponse getPlanById(Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        return PlanResponse.of(plan);
    }

    public void updatePlan(Long planId, PlanRequest planRequest) {

        validateRequest(planRequest);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        AreaDetail areaDetail = areaDetailRepository.getReferenceById(planRequest.getAreaDetailId());
        List<Content> contents = new ArrayList<>();

        if (!planRequest.getContents().isEmpty()) {
            for (Long contentId : planRequest.getContents()) {
                Content content = contentRepository.findById(contentId)
                        .orElseThrow(() -> new RuntimeException("Content not found"));
                contents.add(content);
            }
        }

        plan.setAreaDetail(areaDetail);
        plan.setTitle(planRequest.getTitle());
        plan.setDescription(planRequest.getDescription());
        plan.setStartDate(planRequest.getStartDate());
        plan.setEndDate(planRequest.getEndDate());
        plan.setContents(contents);
        planRepository.save(plan);
    }

    public void deletePlan(Long planId) {

        if (!planRepository.existsById(planId)) {
            throw new RuntimeException("Plan not found");
        }
        planRepository.deleteById(planId);
    }

    public void createLikePlan(Long planId) {

        User user = userHelper.getCurrentUser();
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Like like = Like.builder()
                .user(user)
                .plan(plan)
                .build();

        likeRepository.save(like);
    }

    @Transactional
    public void deleteLikePlan(Long planId) {

        User user = userHelper.getCurrentUser();
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        likeRepository.deleteByUserAndPlan(user, plan);
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
