package com.mono.trigo.openApi.controller;

import com.mono.trigo.openApi.service.OpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/open-api")
public class OpenApiController {

    private final OpenApiService openApiService;

    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @PostMapping("/areas")
    public ResponseEntity<Void> saveAreas() {
        openApiService.saveAreas();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/area-details")
    public ResponseEntity<Void> saveAreaDetails() {
        openApiService.saveAreaDetails();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/content-types")
    public ResponseEntity<Void> saveContentTypes() {
        openApiService.saveContentTypes();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/contents")
    public ResponseEntity<Void> saveContents() {
        openApiService.saveContents();
        return ResponseEntity.ok().build();
    }
}
