package com.mono.trigo.web.content.controller;

import com.mono.trigo.web.content.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping("/area")
    public ResponseEntity<Void> createArea() {

        areaService.createArea();
        return ResponseEntity.ok().build();
    }
}
