package com.mono.trigo.service.plan;

import com.mono.trigo.domain.area.entity.Area;
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
import com.mono.trigo.web.plan.service.PlanService;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    @DisplayName("플랜 생성 성공")
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
}
