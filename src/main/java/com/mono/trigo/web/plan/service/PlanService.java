package com.mono.trigo.web.plan.service;

import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.domain.like.repository.LikeRepository;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.PlanResponse;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import com.mono.trigo.web.review.dto.ReviewListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final UserHelper userHelper;
    private final PlanRepository planRepository;
    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;
    private final AreaDetailRepository areaDetailRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public CreatePlanResponse createPlan(PlanRequest planRequest) {

        User user = userHelper.getCurrentUser();
        AreaDetail areaDetail = areaDetailRepository.findById(planRequest.getAreaDetailId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.AREA_DETAIL_IS_NOT_FOUND));

        List<Content> contents = planRequest.getContents().stream()
                .distinct()
                .map(contentId -> contentRepository.findById(contentId)
                        .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND)))
                .collect(Collectors.toList());

        Plan plan = Plan.of(planRequest, user, areaDetail, contents);

        Plan savedPlan = planRepository.save(plan);

        return CreatePlanResponse.of(savedPlan.getId());
    }

    public List<PlanResponse> getAllPlans() {

        List<Plan> plans = planRepository.findAllByIsPublicTrue();

        return plans.stream()
                .map(PlanResponse::of)
                .collect(Collectors.toList());
    }

    public PlanResponse getPlanById(Long planId) {

        if (planId == null || planId <= 0) {
            throw new ApplicationException(ApplicationError.PLAN_ID_IS_INVALID);
        }

        String redisKey = "plan::" + planId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            return (PlanResponse) redisTemplate.opsForValue().get(redisKey);
        }

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));
        PlanResponse planResponse = PlanResponse.of(plan);

        if (plan.getIsPublic()) {
            redisTemplate.opsForValue().set(redisKey, planResponse, 1, TimeUnit.HOURS);
        }
        return planResponse;
    }

    public void updatePlan(Long planId, PlanRequest planRequest) {

        if (planId == null || planId <= 0) {
            throw new ApplicationException(ApplicationError.PLAN_ID_IS_INVALID);
        }

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));

        AreaDetail areaDetail = areaDetailRepository.findById(planRequest.getAreaDetailId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.AREA_DETAIL_IS_NOT_FOUND));

        if (!plan.getUser().equals(userHelper.getCurrentUser())) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        List<Content> contents = new ArrayList<>();

        if (!planRequest.getContents().isEmpty()) {
            for (Long contentId : planRequest.getContents()) {
                Content content = contentRepository.findById(contentId)
                        .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));
                contents.add(content);
            }
        }

        plan.update(areaDetail, planRequest, contents);
        planRepository.save(plan);
    }

    public void deletePlan(Long planId) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));

        if (!plan.getUser().equals(userHelper.getCurrentUser())) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }

        planRepository.deleteById(planId);
    }

    public void createLikePlan(Long planId) {

        User user = userHelper.getCurrentUser();
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));
        Like like = Like.of(user, plan);

        try {
            likeRepository.save(like);
        } catch (Exception e) {
            throw new ApplicationException(ApplicationError.LIKE_IS_EXISTED);
        }
    }

    @Transactional
    public void deleteLikePlan(Long planId) {

        User user = userHelper.getCurrentUser();
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));

        likeRepository.deleteByUserAndPlan(user, plan);
    }
}
