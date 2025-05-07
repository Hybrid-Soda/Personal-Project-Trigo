package com.mono.trigo.service;

import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.plan.repository.PlanRepository;
import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.user.entity.Gender;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.review.entity.Review;
import com.mono.trigo.domain.user.repository.UserRepository;
import com.mono.trigo.domain.review.repository.ReviewRepository;

import com.mono.trigo.web.plan.dto.PlanListResponse;
import com.mono.trigo.web.review.dto.ReviewListResponse;
import com.mono.trigo.web.user.dto.UserRequest;
import com.mono.trigo.web.user.dto.UserResponse;
import com.mono.trigo.web.user.dto.SignupRequest;
import com.mono.trigo.web.user.service.UserService;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;
    @Mock private UserHelper userHelper;
    @Mock private UserRepository userRepository;
    @Mock private PlanRepository planRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOps;

    private User user;
    private SignupRequest request;

    @BeforeEach
    void setUp() {
        request = SignupRequest.builder()
                .username("testUser123")
                .password("testPassword123")
                .nickname("testNickname123")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .build();

        user = User.of(request, "encodedPassword");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // Given
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        userService.signup(request);

        // Then
        verify(userRepository, times(1)).existsByUsername(request.getUsername());
        verify(bCryptPasswordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals(request.getUsername()) &&
                user.getNickname().equals(request.getNickname()) &&
                user.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    @DisplayName("회원가입 실패: 중복된 사용자 이름")
    void signup_Fail_UsernameExists() {
        // Given
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> userService.signup(request));
        assertEquals(ApplicationError.USERNAME_IS_EXISTED, exception.getError());
    }

    @Test
    @DisplayName("회원 ID로 사용자 정보 조회 성공")
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserResponse response = userService.getUserById(1L);

        // Then
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getNickname(), response.getNickname());
    }

    @Test
    @DisplayName("회원 ID로 사용자 정보 조회 실패: 존재하지 않는 사용자")
    void getUserById_Fail_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> userService.getUserById(1L));
        assertEquals(ApplicationError.USER_IS_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateUser_Success() {
        UserRequest userRequest = UserRequest.builder()
                .nickname("newNickname123")
                .birthday(LocalDate.of(1999, 1, 1))
                .gender(Gender.FEMALE)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userHelper.getCurrentUser()).thenReturn(user);

        // When
        userService.updateUser(1L, userRequest);

        // Then
        verify(userRepository, times(1)).save(argThat(user ->
                user.getNickname().equals(userRequest.getNickname()) &&
                user.getBirthday().equals(userRequest.getBirthday()) &&
                user.getGender().equals(userRequest.getGender())
        ));
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userHelper.getCurrentUser()).thenReturn(user);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("작성한 일정 조회 성공")
    void getPlansByUserId_Success() {
        // Given
        AreaDetail areaDetail = new AreaDetail(1L, new Area(), "name1", "A101");
        Plan plan = Plan.builder()
                .id(1L)
                .user(user)
                .title("title1")
                .contents(new ArrayList<>())
                .areaDetail(areaDetail)
                .isPublic(true)
                .build();

        when(redisTemplate.hasKey("UserPlans::" + 1L)).thenReturn(false);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(planRepository.findByUserId(1L)).thenReturn(List.of(plan));
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // When
        PlanListResponse responses = userService.getPlansByUserId(1L);

        // Then
        assertEquals(1, responses.getPlanResponseList().size());
        assertEquals(plan.getTitle(), responses.getPlanResponseList().get(0).getTitle());
        assertEquals(plan.getIsPublic(), responses.getPlanResponseList().get(0).getIsPublic());
    }

    @Test
    @DisplayName("작성한 리뷰 조회 성공")
    void getReviewsByUserId_Success() {
        // Given
        Review review = Review.builder()
                .id(1L)
                .user(user)
                .content(new Content())
                .rating(5)
                .reviewContent("Great place!")
                .build();

        when(redisTemplate.hasKey("UserReviews::" + 1L)).thenReturn(false);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByUserId(1L)).thenReturn(List.of(review));
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // When
        ReviewListResponse responses = userService.getReviewsByUserId(1L);

        // Then
        assertEquals(1, responses.getReviewResponseList().size());
        assertEquals(review.getReviewContent(), responses.getReviewResponseList().get(0).getReviewContent());
        assertEquals(review.getRating(), responses.getReviewResponseList().get(0).getRating());
    }
}
