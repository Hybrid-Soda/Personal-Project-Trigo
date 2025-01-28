package com.mono.trigo.service.content;

import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.content.repository.ContentRepository;

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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @InjectMocks
    private ContentService contentService;

    @Mock
    private ContentRepository contentRepository;

    private final Area area = new Area(1L, "서울", "1");
    private final AreaDetail areaDetail = new AreaDetail(1L, area, "강남구", "1");
    private final ContentType contentType = new ContentType("A0101", "A01010001", "type1");
    private final Content content1 =
            new Content(1L, contentType, areaDetail, "content1", 10.0, 10.0, "add1", "", "", "", "");
    private final Content content2 =
            new Content(2L, contentType, areaDetail, "content2", 20.0, 20.0, "add1", "", "", "", "");

    @Test
    @DisplayName("컨텐츠 검색 성공")
    void searchContents_Success() {
        // Given
        ContentSearchCondition condition = new ContentSearchCondition("C", "1", "1", "A01010001");
        List<Content> contents = List.of(content1, content2);
        when(contentRepository.searchContents(condition)).thenReturn(contents);

        // When
        List<ContentResponse> responses = contentService.searchContents(condition);

        // Then
        assertEquals(2, responses.size());
        assertEquals(content1.getId(), responses.get(0).getId());
        assertEquals(content2.getId(), responses.get(1).getId());
        verify(contentRepository, times(1)).searchContents(condition);
    }

    @Test
    @DisplayName("컨텐츠 ID로 조회 성공")
    void getContentById_Success() {
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        // When
        ContentResponse response = contentService.getContentById(1L);

        // Then
        assertEquals(content1.getId(), response.getId());
        assertEquals(content1.getTitle(), response.getTitle());
        verify(contentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("컨텐츠 ID로 조회 실패: 유효하지 않은 ID")
    void getContentById_Fail_InvalidId() {
        // Given
        Long invalidId = 0L;

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> contentService.getContentById(invalidId));
        assertEquals(ApplicationError.CONTENT_ID_IS_INVALID, exception.getError());
        verify(contentRepository, never()).findById(any());
    }

    @Test
    @DisplayName("컨텐츠 ID로 조회 실패: 존재하지 않는 컨텐츠")
    void getContentById_Fail_ContentNotFound() {
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> contentService.getContentById(1L));
        assertEquals(ApplicationError.CONTENT_IS_NOT_FOUND, exception.getError());
    }
}
