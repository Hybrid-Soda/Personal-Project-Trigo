package com.mono.trigo.openApi.service;


import com.mono.trigo.openApi.baseDto.WrapperDto;
import com.mono.trigo.openApi.dto.AreaCodeDto;
import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.openApi.util.OpenApiService;
import com.mono.trigo.domain.area.repository.AreaRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AreaService {

    private final AreaRepository areaRepository;
    private final OpenApiService openApiService;

    public AreaService(AreaRepository areaRepository, OpenApiService openApiService) {
        this.areaRepository = areaRepository;
        this.openApiService = openApiService;
    }

    public void saveAreas() {

        ParameterizedTypeReference<WrapperDto<AreaCodeDto>> typeReference = new ParameterizedTypeReference<>(){};
        List<AreaCodeDto> response = openApiService.connectOpenApi("areaCode1", typeReference);

        for (AreaCodeDto areaCodeDto : response) {
            Area area = Area.builder()
                    .name(areaCodeDto.getName())
                    .code(areaCodeDto.getCode())
                    .build();
            areaRepository.save(area);
        }
    }
}
