package com.mono.trigo.web.plan.service;

import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.domain.like.repository.LikeRepository;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.plan.repository.PlanRedisRepository;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.review.entity.Review;
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

import com.mono.trigo.web.plan.dto.PlansResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final UserHelper userHelper;
    private final PlanRepository planRepository;
    private final LikeRepository likeRepository;
    private final ContentRepository contentRepository;
    private final PlanRedisRepository planRedisRepository;
    private final AreaDetailRepository areaDetailRepository;

    @Transactional
    public CreatePlanResponse createPlan(PlanRequest planRequest) {

        User user = userHelper.getCurrentUser();
        AreaDetail areaDetail = getAreaDetail(planRequest.getAreaDetailId());
        List<Content> contents = planRequest.getContents().stream()
                .distinct()
                .map(contentId -> contentRepository.findById(contentId)
                        .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND)))
                .collect(Collectors.toList());

        Plan plan = Plan.of(planRequest, user, areaDetail, contents);
        return CreatePlanResponse.of(planRepository.save(plan).getId());
    }

    public List<PlanResponse> getAllPlans() {

        List<Plan> plans = planRepository.findAllByIsPublicTrue();
        return plans.stream()
                .map(PlanResponse::of)
                .collect(Collectors.toList());
    }

    public PlanResponse getPlanById(Long planId) {

        // 캐시 조회
        Optional<PlanResponse> cachedResponse = planRedisRepository.findById(planId);
        if (cachedResponse.isPresent()) return cachedResponse.get();

        // DB 조회
        PlanResponse planResponse = PlanResponse.of(getPlan(planId));

        // 캐시 저장
        if (planResponse.getIsPublic()) planRedisRepository.save(planResponse);
        return planResponse;
    }

    @Transactional
    public void updatePlan(Long planId, PlanRequest planRequest) {

        Plan plan = getPlan(planId);
        AreaDetail areaDetail = getAreaDetail(planRequest.getAreaDetailId());
        checkUser(plan.getUser());

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
        planRedisRepository.deleteById(planId);
    }

    public void deletePlan(Long planId) {

        Plan plan = getPlan(planId);
        checkUser(plan.getUser());

        planRepository.deleteById(planId);
        planRedisRepository.deleteById(planId);
    }

    public void createLikePlan(Long planId) {

        User user = userHelper.getCurrentUser();
        Plan plan = getPlan(planId);
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
        Plan plan = getPlan(planId);

        likeRepository.deleteByUserAndPlan(user, plan);
    }

    public void checkUser(User planUser) {
        User currentUser = userHelper.getCurrentUser();
        if (!planUser.equals(currentUser)) {
            throw new ApplicationException(ApplicationError.UNAUTHORIZED_ACCESS);
        }
    }

    public Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PLAN_IS_NOT_FOUND));
    }

    public AreaDetail getAreaDetail(Long areaDetailId) {
        return areaDetailRepository.findById(areaDetailId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.AREA_DETAIL_IS_NOT_FOUND));
    }
}
