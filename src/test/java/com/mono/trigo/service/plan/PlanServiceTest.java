package com.mono.trigo.service.plan;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.like.repository.LikeRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;

import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.service.PlanService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

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

    private PlanRequest planRequest;
    private User user;
    private AreaDetail areaDetail;
    private Plan plan;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser123")
                .build();

        areaDetail = AreaDetail.builder()
                .id(1L)
                .name("Test Area")
                .build();

        planRequest = PlanRequest.builder()
                .title("Test Plan")
                .description("This is a test plan.")
                .areaDetailId(1L)
                .contents(List.of(1L, 2L))
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 10))
                .isPublic(true)
                .build();

        plan = Plan.builder()
                .id(1L)
                .user(user)
                .areaDetail(areaDetail)
                .title(planRequest.getTitle())
                .description(planRequest.getDescription())
                .startDate(planRequest.getStartDate())
                .endDate(planRequest.getEndDate())
                .isPublic(planRequest.getIsPublic())
                .build();
    }

    @Test
    @DisplayName("플랜 생성 성공")
    void createPlan_Success() {
        // Given
        Content content1 = Content.builder().id(1L).build();
        Content content2 = Content.builder().id(2L).build();

        when(userHelper.getCurrentUser()).thenReturn(user);
        when(areaDetailRepository.findById(planRequest.getAreaDetailId())).thenReturn(Optional.of(areaDetail));
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));
        when(contentRepository.findById(2L)).thenReturn(Optional.of(content2));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // When
        CreatePlanResponse response = planService.createPlan(planRequest);

        // Then
        assertEquals(plan.getId(), response.getPlanId());
        verify(planRepository, times(1)).save(any(Plan.class));
    }
}
