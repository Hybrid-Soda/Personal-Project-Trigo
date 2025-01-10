package com.mono.trigo.openApi.controller;

import com.mono.trigo.openApi.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/area")
    public ResponseEntity<Void> saveAreas() {
        areaService.saveAreas();
        return ResponseEntity.ok().build();
    }
}
