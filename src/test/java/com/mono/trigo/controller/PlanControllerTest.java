package com.mono.trigo.controller;

import com.mono.trigo.web.plan.dto.PlanRequest;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class PlanControllerTest {

    @InjectMocks
    private PlanController planController;

    @Mock
    private PlanService planService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
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

        when(planService.createPlan(planRequest)).thenReturn(planResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(planRequest))
                        .header("access", "accessToken"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Plan"));
    }
}
