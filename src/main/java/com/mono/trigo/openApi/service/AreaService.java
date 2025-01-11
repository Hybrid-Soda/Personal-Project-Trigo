package com.mono.trigo.openApi.service;


import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;
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

    private final OpenApiService openApiService;
    private final AreaRepository areaRepository;
    private final AreaDetailRepository areaDetailRepository;

    public AreaService(
            OpenApiService openApiService,
            AreaRepository areaRepository,
            AreaDetailRepository areaDetailRepository) {

        this.openApiService = openApiService;
        this.areaRepository = areaRepository;
        this.areaDetailRepository = areaDetailRepository;
    }

    public void saveAreas() {

        ParameterizedTypeReference<WrapperDto<AreaCodeDto>> typeReference = new ParameterizedTypeReference<>(){};
        List<AreaCodeDto> response = openApiService.connectOpenApi("areaCode1", typeReference, null);

        for (AreaCodeDto areaCodeDto : response) {
            Area area = Area.builder()
                    .name(areaCodeDto.getName())
                    .code(areaCodeDto.getCode())
                    .build();
            areaRepository.save(area);
        }
    }

    public void saveAreaDetails() {

        List<Area> areas = areaRepository.findAll();
        ParameterizedTypeReference<WrapperDto<AreaCodeDto>> typeReference = new ParameterizedTypeReference<>(){};

        for (Area area : areas) {
            List<AreaCodeDto> response = openApiService.connectOpenApi("areaCode1", typeReference, area.getCode());

            for (AreaCodeDto areaCodeDto : response) {
                AreaDetail areadetail = AreaDetail.builder()
                        .area(area)
                        .name(areaCodeDto.getName())
                        .code(areaCodeDto.getCode())
                        .build();
                areaDetailRepository.save(areadetail);
            }
        }

    }
}
