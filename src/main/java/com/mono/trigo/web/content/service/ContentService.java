package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.repository.ContentRedisRepository;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.mono.trigo.web.exception.advice.ApplicationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentService {

    final int DEFAULT_PAGE_NUMBER = 0;
    final int DEFAULT_NUM_OF_ROWS = 100;

    private final ContentRepository contentRepository;
    private final ContentRedisRepository contentRedisRepository;

    public ContentService(ContentRepository contentRepository, ContentRedisRepository contentRedisRepository) {
        this.contentRepository = contentRepository;
        this.contentRedisRepository = contentRedisRepository;
    }


    // 여행지 목록 조회
    public Page<ContentResponse> searchContents(ContentSearchCondition condition, Integer numOfRows, Integer pageNo) {

        PageRequest pageRequest = createPageRequest(pageNo, numOfRows);
        return contentRepository.searchContents(condition, pageRequest).map(ContentResponse::of);
    }

    // 여행지 상세 조회
    public ContentResponse getContentById(Long contentId) {

        // 캐시 조회
        Optional<ContentResponse> cachedResponse = contentRedisRepository.findById(String.valueOf(contentId));
        if (cachedResponse.isPresent()) {
            return cachedResponse.get();
        }

        // DB 조회
        ContentResponse contentResponse = ContentResponse.of(contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND)));

        // 캐시 저장
        contentRedisRepository.save(contentResponse);
        return contentResponse;
    }

    private PageRequest createPageRequest(Integer pageNo, Integer numOfRows) {
        int page = Optional.ofNullable(pageNo).orElse(DEFAULT_PAGE_NUMBER);
        int rows = Optional.ofNullable(numOfRows).orElse(DEFAULT_NUM_OF_ROWS);
        return PageRequest.of(Math.max(page-1, 0), rows);
    }
}
