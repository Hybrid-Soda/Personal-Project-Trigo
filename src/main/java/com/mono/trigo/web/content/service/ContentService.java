package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.web.content.dto.ContentResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<ContentResponse> getContents(Long contentId) {
        List<ContentResponse> contents = new ArrayList<>();

        return contents;
    }

    public ContentResponse getContentById(Long contentId) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content is null"));

        return ContentResponse.of(content);
    }
}
