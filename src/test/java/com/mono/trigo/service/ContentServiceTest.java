package com.mono.trigo.service;

import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.content.entity.ContentCount;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.content.repository.ContentRedisRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.content.service.ContentService;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @InjectMocks
    private ContentService contentService;

    @Mock private ContentRepository contentRepository;
    @Mock private ContentRedisRepository contentRedisRepository;

    private final Long contentId = 1L;
    private final Area area = new Area(1L, "서울", "1");
    private final AreaDetail areaDetail = new AreaDetail(1L, area, "강남구", "1");
    private final ContentType contentType = new ContentType("A0101", "A01010001", "type1");
    private  final ContentCount contentCount = new ContentCount(contentId, 1, 4.0f);
    private final Content content1 = Content.builder()
            .id(contentId).contentType(contentType).areaDetail(areaDetail).contentCount(contentCount)
            .build();
    private final Content content2 = Content.builder()
            .id(2L).contentType(contentType).areaDetail(areaDetail).contentCount(contentCount)
            .build();
    private final ContentResponse contentResponse = ContentResponse.of(content1);

    @Test
    @DisplayName("컨텐츠 검색 성공")
    void searchContents_Success() {
        // Given
        ContentSearchCondition condition = new ContentSearchCondition("C", "1", "1", "A01010001");
        List<Content> contents = List.of(content1, content2);
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Content> page = new PageImpl<>(contents, pageRequest, contents.size());

        when(contentRepository.searchContents(eq(condition), any(Pageable.class))).thenReturn(page);

        // When
        Page<ContentResponse> responses = contentService.searchContents(condition, 10, 1);

        // Then
        assertEquals(2, responses.getContent().size());
        assertEquals(content1.getId(), responses.getContent().get(0).getId());
        assertEquals(content2.getId(), responses.getContent().get(1).getId());
        verify(contentRepository, times(1)).searchContents(eq(condition), any(Pageable.class));
    }

    @Test
    @DisplayName("컨텐츠 ID 조회 성공: 캐시 히트")
    void getContentById_Success_CacheHit() {
        // Given
        Long contentId = content1.getId();
        when(contentRedisRepository.findById(contentId)).thenReturn(Optional.of(contentResponse));

        // When
        ContentResponse response = contentService.getContentById(contentId);

        // Then
        assertThat(response).isEqualTo(contentResponse);
        verify(contentRedisRepository, times(1)).findById(contentId);
        verifyNoInteractions(contentRepository);
    }

    @Test
    @DisplayName("컨텐츠 ID 조회 성공: 캐시 미스 후 DB 조회")
    void getContentById_Success_DB() {
        // Given
        when(contentRedisRepository.findById(contentId)).thenReturn(Optional.empty());
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content1));

        // When
        ContentResponse response = contentService.getContentById(contentId);

        // Then
        assertThat(response.getId()).isEqualTo(contentResponse.getId());
        verify(contentRepository, times(1)).findById(contentId);
    }

    @Test
    @DisplayName("컨텐츠 ID 조회 실패: 존재하지 않는 컨텐츠")
    void getContentById_Fail_ContentNotFound() {
        // Given
        when(contentRedisRepository.findById(contentId)).thenReturn(Optional.empty());
        when(contentRepository.findById(contentId)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception =
                assertThrows(ApplicationException.class, () -> contentService.getContentById(contentId));
        assertEquals(ApplicationError.CONTENT_IS_NOT_FOUND, exception.getError());
    }
}
