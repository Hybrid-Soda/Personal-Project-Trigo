package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.mono.trigo.web.exception.advice.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentService {

    final int DEFAULT_PAGE_NUMBER = 0;
    final int DEFAULT_NUM_OF_ROWS = 100;
    private final ContentRepository contentRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // 여행지 목록 조회
    public Page<ContentResponse> searchContents(ContentSearchCondition condition, Integer numOfRows, Integer pageNo) {
        PageRequest pageRequest = createPageRequest(pageNo, numOfRows);

        return contentRepository.searchContents(condition, pageRequest)
                                .map(ContentResponse::of);
    }

    // 여행지 상세 조회
    public ContentResponse getContentById(Long contentId) {

        String redisKey = "content::" + contentId;
        if (redisTemplate.hasKey(redisKey)) {
            return (ContentResponse) redisTemplate.opsForValue().get(redisKey);
        }

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));

        redisTemplate.opsForValue().set(redisKey, ContentResponse.of(content));

        return ContentResponse.of(content);
    }

    private PageRequest createPageRequest(Integer pageNo, Integer numOfRows) {
        int page = Optional.ofNullable(pageNo).orElse(DEFAULT_PAGE_NUMBER);
        int rows = Optional.ofNullable(numOfRows).orElse(DEFAULT_NUM_OF_ROWS);
        return PageRequest.of(Math.max(page-1, 0), rows);
    }
}
