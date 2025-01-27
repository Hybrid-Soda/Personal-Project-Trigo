package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.mono.trigo.web.exception.advice.ApplicationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<ContentResponse> searchContents(ContentSearchCondition condition) {

        List<Content> contents = contentRepository.searchContents(condition);

        return contents.stream()
                .map(ContentResponse::of)
                .collect(Collectors.toList());
    }

    public ContentResponse getContentById(Long contentId) {

        if (contentId == null || contentId <= 0) {
            throw new ApplicationException(ApplicationError.CONTENT_ID_IS_INVALID);
        }

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.CONTENT_IS_NOT_FOUND));

        return ContentResponse.of(content);
    }
}
