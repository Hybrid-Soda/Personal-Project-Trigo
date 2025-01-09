package com.mono.trigo.openApi;

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
    public ResponseEntity<String> createArea() {

        String response = areaService.createArea();
        return ResponseEntity.ok().body(response);
    }
}
