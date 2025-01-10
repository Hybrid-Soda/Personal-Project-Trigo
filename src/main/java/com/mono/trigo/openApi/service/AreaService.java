package com.mono.trigo.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mono.trigo.domain.area.repository.AreaRepository;
import com.mono.trigo.openApi.util.OpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AreaService {

    private final AreaRepository areaRepository;
    private final OpenApiService openApiService;

    public AreaService(AreaRepository areaRepository, OpenApiService openApiService) {
        this.areaRepository = areaRepository;
        this.openApiService = openApiService;
    }

    public void createArea() {

        JsonNode response = openApiService.connectOpenApi("areaCode1");

    }
}
