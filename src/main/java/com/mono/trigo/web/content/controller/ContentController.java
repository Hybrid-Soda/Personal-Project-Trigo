package com.mono.trigo.web.content.controller;

import com.mono.trigo.web.content.dto.ContentResponse;
import com.mono.trigo.common.audit.aop.LogExecutionTime;
import com.mono.trigo.web.content.service.ContentService;
import com.mono.trigo.web.content.dto.ContentSearchCondition;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    @LogExecutionTime
    public ResponseEntity<Page<ContentResponse>> searchContents(
            @Nullable @RequestParam String arrange,
            @Nullable @RequestParam String areaCode,
            @Nullable @RequestParam String areaDetailCode,
            @Nullable @RequestParam String contentTypeCode,
            @Nullable @RequestParam Integer numOfRows,
            @Nullable @RequestParam Integer pageNo
    ) {

        ContentSearchCondition condition = new ContentSearchCondition(arrange, areaCode, areaDetailCode, contentTypeCode);
        Page<ContentResponse> response = contentService.searchContents(condition, numOfRows, pageNo);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{contentId}")
    @LogExecutionTime
    public ResponseEntity<ContentResponse> getContentById(@PathVariable @Min(1) Long contentId) {

        ContentResponse response = contentService.getContentById(contentId);
        return ResponseEntity.status(200).body(response);
    }
}
