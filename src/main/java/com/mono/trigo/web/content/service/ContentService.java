package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.mono.trigo.web.exception.advice.ApplicationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ContentService {

    final int DEFAULT_PAGE_NUMBER = 0;
    final int DEFAULT_NUM_OF_ROWS = 100;
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Page<ContentResponse> searchContents(ContentSearchCondition condition, Integer numOfRows, Integer pageNo) {
        PageRequest pageRequest = createPageRequest(pageNo, numOfRows);

        return contentRepository.searchContents(condition, pageRequest)
                                .map(ContentResponse::of);
    }

    public ContentResponse getContentById(Long contentId) {

        if (contentId == null || contentId <= 0) {
            throw new ApplicationException(ApplicationError.CONTENT_ID_IS_INVALID);
        }

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));

        return ContentResponse.of(content);
    }

    private PageRequest createPageRequest(Integer pageNo, Integer numOfRows) {
        int page = Optional.ofNullable(pageNo).orElse(DEFAULT_PAGE_NUMBER);
        int rows = Optional.ofNullable(numOfRows).orElse(DEFAULT_NUM_OF_ROWS);
        return PageRequest.of(Math.max(page-1, 0), rows);
    }
}
