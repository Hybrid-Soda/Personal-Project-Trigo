package com.mono.trigo.openApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mono.trigo.openApi.dto.ResponseDto;
import com.mono.trigo.openApi.dto.WrapperDto;
import com.mono.trigo.openApi.util.OpenApiService;
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

    public void createArea() {

        WrapperDto response = openApiService.connectOpenApi("areaCode1");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            System.out.println("Response as JSON String: ");
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
        System.out.println(response.getResponse());
        System.out.println(response.getResponse().getBody().getNumOfRows());
        System.out.println(response.getResponse().getHeader().getResultCode());
        System.out.println(response.getResponse().getBody().getItems().getItem().get(1));

    }
}
