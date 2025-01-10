package com.mono.trigo.openApi;

import com.mono.trigo.domain.area.repository.AreaRepository;
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

    public String createArea() {

        String response = openApiService.connectOpenApi("http://apis.data.go.kr/B551011/KorService1/areaCode1");
        System.out.println("API Response: " + response);
        return response;
    }
}
