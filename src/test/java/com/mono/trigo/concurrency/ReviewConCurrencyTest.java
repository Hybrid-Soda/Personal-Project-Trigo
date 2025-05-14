package com.mono.trigo.concurrency;

import com.mono.trigo.domain.user.entity.User;
import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.web.review.service.ReviewService;
import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.area.repository.AreaRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;
import com.mono.trigo.domain.content.repository.ContentTypeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReviewConCurrencyTest {

    @Autowired private ReviewService reviewService;
    @Autowired private AreaRepository areaRepository;
    @Autowired private ContentRepository contentRepository;
    @Autowired private AreaDetailRepository areaDetailRepository;
    @Autowired private ContentTypeRepository contentTypeRepository;

    private final User user1 = User.builder().id(1L).username("n1").role("member").build();
    private final User user2 = User.builder().id(2L).username("n2").role("member").build();

    private final Area area = Area.builder().name("서울").code("1").build();
    private final AreaDetail areaDetail = AreaDetail.builder().name("강남구").code("A01").area(area).build();
    private final ContentType contentType = new ContentType("A0101", "A01010001", "type1");

    @BeforeEach
    void setUp() {
        Content content = Content.builder().title("테스트 콘텐츠")
                .contentType(contentType).areaDetail(areaDetail).build();

        areaRepository.save(area);
        areaDetailRepository.save(areaDetail);
        contentTypeRepository.save(contentType);
        contentRepository.save(content);
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void lostUpdateTest() throws InterruptedException {
        Optional<Content> content = contentRepository.findById(1L);
        System.out.println(content);
    }

}