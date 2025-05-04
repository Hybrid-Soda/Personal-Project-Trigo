package com.mono.trigo.controller;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.entity.Gender;

import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.service.UserService;
import com.mono.trigo.web.review.dto.ReviewResponse;
import com.mono.trigo.web.user.service.ReissueService;
import com.mono.trigo.web.user.controller.UserController;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ReissueService reissueService;

    private MockMvc mockMvc;
    private User user;
    private SignupRequest signupRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        signupRequest = SignupRequest.builder()
                .username("testUser123")
                .password("testPassword123")
                .nickname("testNickname123")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .build();

        user = User.of(signupRequest, "encodedPassword");
        user.setId(1L);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void signup_Success() throws Exception {
        // Given
        doNothing().when(userService).signup(any(SignupRequest.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // Then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"));
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getUserById_Success() throws Exception {
        // Given
        UserResponse userResponse = UserResponse.of(user);
        when(userService.getUserById(1L)).thenReturn(userResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/1"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.nickname").value(user.getNickname()));
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUser_Success() throws Exception {
        // Given
        UserRequest userRequest = new UserRequest("newNickname", LocalDate.of(2000, 1, 1), Gender.FEMALE);
        doNothing().when(userService).updateUser(eq(1L), any(UserRequest.class));

        // When
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUser_Success() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/1"));

        // Then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자가 작성한 리뷰 조회 성공")
    void getReviewsByUserId_Success() throws Exception {
        // Given
        ReviewResponse review1 = ReviewResponse.builder().reviewId(1L).rating(5).reviewContent("Great place!").build();
        ReviewResponse review2 = ReviewResponse.builder().reviewId(2L).rating(1).reviewContent("pool..").build();
        ReviewListResponse reviews = ReviewListResponse.of(List.of(review1, review2));

        when(userService.getReviewsByUserId(1L)).thenReturn(reviews);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/1/reviews"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewResponseList.length()").value(2))
                .andExpect(jsonPath("$.reviewResponseList[0].reviewId").value(1L))
                .andExpect(jsonPath("$.reviewResponseList[0].reviewContent").value("Great place!"))
                .andExpect(jsonPath("$.reviewResponseList[1].reviewId").value(2L))
                .andExpect(jsonPath("$.reviewResponseList[1].reviewContent").value("pool.."));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_Success() throws Exception {
        // Given
        Cookie refreshCookie = new Cookie("refresh", "newRefreshToken");

        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(1, HttpServletResponse.class);
            res.setHeader("access", "newAccessToken");
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(60*60*24);
            res.addCookie(refreshCookie);
            return null;
        }).when(reissueService).reissue(any(HttpServletRequest.class), any(HttpServletResponse.class));


        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/users/reissue"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("access"))
                .andExpect(header().string("access", "newAccessToken"))
                .andExpect(cookie().exists("refresh"))
                .andExpect(cookie().value("refresh", "newRefreshToken"))
                .andExpect(cookie().httpOnly("refresh", true))
                .andExpect(cookie().maxAge("refresh", 60*60*24));
    }
}
