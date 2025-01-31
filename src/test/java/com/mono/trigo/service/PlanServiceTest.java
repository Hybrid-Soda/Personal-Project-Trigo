package com.mono.trigo.service;

import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.like.repository.LikeRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;

import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.PlanResponse;
import com.mono.trigo.web.plan.service.PlanService;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @InjectMocks
    private PlanService planService;

    @Mock
    private UserHelper userHelper;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private AreaDetailRepository areaDetailRepository;

    private final User user = User.builder().id(1L).username("testUser123").build();
    private final Area area = new Area(1L, "서울", "1");
    private final AreaDetail areaDetail = new AreaDetail(1L, area, "강남구", "1");
    private final Content content1 = Content.builder().id(1L).build();
    private final Content content2 = Content.builder().id(2L).build();

    private PlanRequest planRequest;
    private Plan plan;

    @BeforeEach
    void setUp() {
        planRequest = PlanRequest.builder()
                .title("Test Plan")
                .description("This is a test plan.")
                .areaDetailId(1L)
                .contents(List.of(1L, 2L))
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 10))
                .isPublic(true)
                .build();

        plan = Plan.of(planRequest, user, areaDetail, List.of(content1, content2));
        plan.setId(1L);
    }

    @Test
    @DisplayName("일정 생성 성공")
    void createPlan_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(areaDetailRepository.findById(planRequest.getAreaDetailId())).thenReturn(Optional.of(areaDetail));
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));
        when(contentRepository.findById(2L)).thenReturn(Optional.of(content2));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // When
        CreatePlanResponse response = planService.createPlan(planRequest);

        // Then
        assertEquals(plan.getId(), response.getPlanId());
        verify(planRepository, times(1)).save(argThat(plan ->
                plan.getAreaDetail().equals(areaDetail) &&
                plan.getContents().equals(List.of(content1, content2)) &&
                plan.getTitle().equals(planRequest.getTitle()) &&
                plan.getDescription().equals(planRequest.getDescription()) &&
                plan.getStartDate().equals(planRequest.getStartDate()) &&
                plan.getEndDate().equals(planRequest.getEndDate())
        ));
    }

    @Test
    @DisplayName("일정 생성 실패: 존재하지 않는 지역")
    void createPlan_Fail_AreaDetailNotFound() {
        // Given
        when(areaDetailRepository.findById(planRequest.getAreaDetailId())).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> planService.createPlan(planRequest));
        assertEquals(ApplicationError.AREA_DETAIL_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("전체 일정 조회 성공")
    void getAllPlans_Success() {
        // Given
        Plan plan1 = Plan.builder().id(1L).title("plan1").user(user).build();
        Plan plan2 = Plan.builder().id(2L).title("plan2").user(user).build();

        when(planRepository.findAll()).thenReturn(List.of(plan1, plan2));

        // When
        List<PlanResponse> plans = planService.getAllPlans();

        // Then
        assertEquals(2, plans.size());
        assertEquals(plan1.getId(), plans.get(0).getPlanId());
        assertEquals(plan2.getId(), plans.get(1).getPlanId());
    }

    @Test
    @DisplayName("일정 조회 성공")
    void getPlanById_Success() {
        // Given
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // When
        PlanResponse planResponse = planService.getPlanById(1L);

        //Then
        assertEquals(1L, planResponse.getPlanId());
        assertEquals(plan.getTitle(), planResponse.getTitle());
        assertEquals(plan.getDescription(), planResponse.getDescription());
    }

    @Test
    @DisplayName("일정 조회 실패: 존재하지 않는 일정")
    void getPlanById_Fail_PlanNotFound() {
        // Given
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> planService.getPlanById(1L));
        assertEquals(ApplicationError.PLAN_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("일정 수정 성공")
    void updatePlan_Success() {
        // Given
        planRequest.setContents(List.of(1L));
        planRequest.setTitle("Updated Plan");
        planRequest.setDescription("Updated description");

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(areaDetailRepository.findById(planRequest.getAreaDetailId())).thenReturn(Optional.of(areaDetail));
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        // When
        planService.updatePlan(1L, planRequest);

        // Then
        verify(planRepository, times(1)).save(argThat(plan ->
                plan.getContents().equals(List.of(content1)) &&
                plan.getTitle().equals(planRequest.getTitle()) &&
                plan.getDescription().equals(planRequest.getDescription())));
    }

    @Test
    @DisplayName("일정 수정 실패: 존재하지 않는 일정")
    void updatePlan_Fail_PlanNotFound() {
        // Given
        when(planRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> planService.updatePlan(2L, planRequest));
        assertEquals(ApplicationError.PLAN_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("일정 수정 실패: 권한 없음")
    void updatePlan_Fail_UnauthorizedAccess() {
        // Given
        User otherUser = User.builder().id(2L).username("otherUser123").build();
        plan.setUser(otherUser);

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(areaDetailRepository.findById(planRequest.getAreaDetailId())).thenReturn(Optional.of(areaDetail));

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> planService.updatePlan(1L, planRequest));
        assertEquals(ApplicationError.UNAUTHORIZED_ACCESS, exception.getError());
    }

    @Test
    @DisplayName("일정 삭제 성공")
    void deletePlan_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // When
        planService.deletePlan(1L);

        // Then
        verify(planRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("일정 좋아요 생성 성공")
    void createLikePlan_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // When
        planService.createLikePlan(1L);

        // Then
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("일정 좋아요 생성 실패: 이미 존재하는 좋아요")
    void createLikePlan_Fail_LikeAlreadyExists() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        doThrow(new DataIntegrityViolationException("Duplicate entry")).when(likeRepository).save(any(Like.class));

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> planService.createLikePlan(1L));
        assertEquals(ApplicationError.LIKE_IS_EXISTED, exception.getError());
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("일정 좋아요 삭제 성공")
    void deleteLikePlan_Success() {
        // Given
        when(userHelper.getCurrentUser()).thenReturn(user);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // When
        planService.deleteLikePlan(1L);

        // Then
        verify(likeRepository, times(1)).deleteByUserAndPlan(user, plan);
    }
}
