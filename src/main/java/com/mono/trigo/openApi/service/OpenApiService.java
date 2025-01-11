package com.mono.trigo.openApi.service;


import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.content.repository.ContentTypeRepository;
import com.mono.trigo.openApi.dto.AreaCodeDto;
import com.mono.trigo.openApi.baseDto.WrapperDto;

import com.mono.trigo.domain.area.entity.Area;
import com.mono.trigo.domain.area.entity.AreaDetail;
import com.mono.trigo.domain.area.repository.AreaRepository;
import com.mono.trigo.domain.area.repository.AreaDetailRepository;

import com.mono.trigo.openApi.util.OpenApiHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OpenApiService {

    private final OpenApiHelper openApiHelper;
    private final AreaRepository areaRepository;
    private final AreaDetailRepository areaDetailRepository;
    private final ContentRepository contentRepository;
    private final ContentTypeRepository contentTypeRepository;

    public void saveAreas() {

        ParameterizedTypeReference<WrapperDto<AreaCodeDto>> typeReference = new ParameterizedTypeReference<>(){};
        List<AreaCodeDto> response = openApiHelper.connectOpenApi("areaCode1", typeReference, null);

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
            Map<String, String> addParameter = new HashMap<>();
            addParameter.put("areaCode", area.getCode());

            List<AreaCodeDto> response = openApiHelper.connectOpenApi("areaCode1", typeReference, addParameter);

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

    public void saveContentTypes() {

        ParameterizedTypeReference<WrapperDto<AreaCodeDto>> typeReference = new ParameterizedTypeReference<>(){};
        List<AreaCodeDto> response = openApiHelper.connectOpenApi("categoryCode1", typeReference, null);

        for (AreaCodeDto areaCodeDto : response) {
            ContentType contentType = ContentType.builder()
                    .parentCode("")
                    .code("")
                    .name("")
                    .build();
        }
    }

    public void saveContents() {

    }
}
