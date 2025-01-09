package com.mono.trigo.web.content.controller;

import com.mono.trigo.web.content.service.ContentService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<String> createContent() {

        return ResponseEntity.status(201).body("");
    }

    @GetMapping
    public ResponseEntity<String> getContent() {

        return ResponseEntity.status(200).body("");
    }
}
