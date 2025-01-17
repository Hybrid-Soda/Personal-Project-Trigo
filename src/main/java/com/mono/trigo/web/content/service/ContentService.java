package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.web.content.dto.ContentResponse;

import com.mono.trigo.web.content.dto.ContentSearchCondition;
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

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content is null"));

        return ContentResponse.of(content);
    }
}
