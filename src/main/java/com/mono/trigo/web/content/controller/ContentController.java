package com.mono.trigo.web.content.controller;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.web.content.service.ContentService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getContent() {
        List<ContentResponse> response = contentService.getContents(1L);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long contentId) {
        ContentResponse response = contentService.getContentById(contentId);
        return ResponseEntity.status(200).body(response);
    }
}
