package com.mono.trigo.concurrency;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.user.entity.Gender;
import com.mono.trigo.domain.user.impl.UserHelper;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.area.repository.AreaRepository;
import com.mono.trigo.domain.user.repository.UserRepository;
import com.mono.trigo.domain.review.repository.ReviewRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;
import com.mono.trigo.domain.content.repository.ContentTypeRepository;
import com.mono.trigo.domain.content.repository.ContentRedisRepository;

import com.mono.trigo.web.review.dto.ReviewRequest;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.web.content.service.ContentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ReviewConCurrencyTest {

    @Autowired private ReviewService reviewService;
    @Autowired private ContentService contentService;

    @Autowired private UserRepository userRepository;
    @Autowired private AreaRepository areaRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ContentRepository contentRepository;
    @Autowired private AreaDetailRepository areaDetailRepository;
    @Autowired private ContentTypeRepository contentTypeRepository;

    @MockitoBean private UserHelper userHelper;
    @MockitoBean private ContentRedisRepository contentRedisRepository;

    private final User user1 = User.builder()
            .username("un1").password("p1").nickname("nn1").birthday(LocalDate.of(2000, 1, 1)).gender(Gender.MALE)
            .build();
    private final User user2 = User.builder()
            .username("un2").password("p2").nickname("nn2").birthday(LocalDate.of(2000, 1, 1)).gender(Gender.FEMALE)
            .build();

    private final Area area = Area.builder().name("서울").code("1").build();
    private final AreaDetail areaDetail = AreaDetail.builder().name("강남구").code("A01").area(area).build();
    private final ContentType contentType = new ContentType("A0101", "A01010001", "type1");
    private final ReviewRequest reviewRequest1 = new ReviewRequest(5, "very good", "p1");
    private final ReviewRequest reviewRequest2 = new ReviewRequest(4, "good", "p2");
    private final Content content = Content.builder()
            .title("테스트 콘텐츠").mapX(131d).mapY(37d).contentType(contentType).areaDetail(areaDetail)
            .build();


    @BeforeEach
    void setUp() {
        userRepository.save(user1);
        userRepository.save(user2);
        areaRepository.save(area);
        areaDetailRepository.save(areaDetail);
        contentTypeRepository.save(contentType);
        contentRepository.save(content);
    }

    @Test
    @DisplayName("콘텐츠 통계 누락")
    void lostUpdateTest() throws InterruptedException {
        // Given
        doNothing().when(contentRedisRepository).deleteById(1L);
        // 동시 실행할 스레드 수 설정 및 스레드 풀 생성
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // ThreadLocal을 사용해 스레드별 User를 분리 저장
        ThreadLocal<User> threadUser = ThreadLocal.withInitial(() -> null);
        doAnswer(invocation -> threadUser.get()).when(userHelper).getCurrentUser();

        // 스레드 준비 완료를 알리는 래치: 모든 스레드가 시작 신호 전까지 대기
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);

        // 스레드별 유저 설정 및 작업 정의
        Runnable user1Task = () -> {
            threadUser.set(user1);
            ready.countDown();
            try {
                start.await();
                reviewService.createReview(1L, reviewRequest1);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
        Runnable user2Task = () -> {
            threadUser.set(user2);
            ready.countDown();
            try {
                start.await();
                reviewService.createReview(1L, reviewRequest2);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        // When
        // 스레드 실행
        executor.submit(user1Task);
        executor.submit(user2Task);

        // 모든 스레드 준비 후 동시 시작
        ready.await();
        start.countDown();

        // 스레드풀 종료
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Then
        // 통계 검증 (리뷰 수 2, 평균 점수 4.5 예상)
        Content updated = contentRepository.findById(1L).orElseThrow();
        assertEquals(2, updated.getContentCount().getReviewCount());
        assertEquals(4.5f, updated.getContentCount().getAverageScore());
    }
}