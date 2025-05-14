package com.mono.trigo.controller;

import com.mono.trigo.web.plan.dto.PlanRequest;
import com.mono.trigo.web.plan.dto.PlanResponse;
import com.mono.trigo.web.plan.service.PlanService;
import com.mono.trigo.web.plan.dto.CreatePlanResponse;
import com.mono.trigo.web.plan.controller.PlanController;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @InjectMocks
    private PlanController planController;

    @Mock
    private PlanService planService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(planController).build();
    }

    @Test
    @DisplayName("일정 생성 성공")
    void createPlan_Success() throws Exception {
        // Given
        PlanRequest planRequest = PlanRequest.builder()
                .areaDetailId(1L)
                .contents(List.of(1L, 2L))
                .title("title")
                .description("description")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 5))
                .isPublic(false)
                .build();

        CreatePlanResponse planResponse = CreatePlanResponse.builder().planId(1L).build();

        when(planService.createPlan(any(PlanRequest.class))).thenReturn(planResponse);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planRequest)));

        // Then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.planId").value(1L));
    }

    @Test
    @DisplayName("전체 일정 조회 성공")
    void getAllPlans_Success() throws Exception {
        // Given
        PlanResponse planResponse1 = PlanResponse.builder().planId(1L).build();
        PlanResponse planResponse2 = PlanResponse.builder().planId(2L).build();

        List<PlanResponse> response = List.of(planResponse1, planResponse2);
        when(planService.getAllPlans()).thenReturn(response);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/plans"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].planId").value(1L))
                .andExpect(jsonPath("$[1].planId").value(2L));
    }

    @Test
    @DisplayName("특정 일정 조회 성공")
    void getPlanById_Success() throws Exception {
        // Given
        PlanResponse planResponse = PlanResponse.builder().planId(1L).build();
        when(planService.getPlanById(1L)).thenReturn(planResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/plans/1"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(1L));
    }

    @Test
    @DisplayName("일정 수정 성공")
    void updatePlan_Success() throws Exception {
        // Given
        PlanRequest planRequest = PlanRequest.builder()
                .areaDetailId(1L)
                .contents(List.of(1L, 2L))
                .title("updated title")
                .description("updated description")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 10))
                .isPublic(true)
                .build();

        doNothing().when(planService).updatePlan(eq(1L), any(PlanRequest.class));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/plans/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planRequest)));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("Plan updated successfully"));
    }

    @Test
    @DisplayName("일정 삭제 성공")
    void deletePlan_Success() throws Exception {
        // Given
        doNothing().when(planService).deletePlan(1L);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/plans/1"));

        // Then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("일정 좋아요 추가 성공")
    void createLikePlan_Success() throws Exception {
        // Given
        doNothing().when(planService).createLikePlan(1L);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/plans/1/likes"));

        // Then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string("Like created successfully"));
    }

    @Test
    @DisplayName("일정 좋아요 해제 성공")
    void deleteLikePlan_Success() throws Exception {
        // Given
        doNothing().when(planService).deleteLikePlan(1L);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/plans/1/likes"));

        // Then
        resultActions
                .andExpect(status().isNoContent());
    }
}
