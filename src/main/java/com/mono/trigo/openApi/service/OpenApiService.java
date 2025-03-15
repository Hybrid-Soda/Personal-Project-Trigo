package com.mono.trigo.openApi.service;


import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.entity.ContentType;
import com.mono.trigo.domain.content.repository.ContentRepository;
import com.mono.trigo.domain.content.repository.ContentTypeRepository;
import com.mono.trigo.openApi.dto.ContentDto;
import com.mono.trigo.openApi.dto.NameCodeDto;
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
    private final ContentRepository contentRepository;
    private final AreaDetailRepository areaDetailRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final ParameterizedTypeReference<WrapperDto<NameCodeDto>> typeReference
            = new ParameterizedTypeReference<>() {};

    public void saveAreas() {

        List<NameCodeDto> response = openApiHelper.connectOpenApi("areaCode1", typeReference, null);

        for (NameCodeDto nameCodeDto : response) {
            Area area = Area.builder()
                    .name(nameCodeDto.getName())
                    .code(nameCodeDto.getCode())
                    .build();
            areaRepository.save(area);
        }
    }

    public void saveAreaDetails() {

        List<Area> areas = areaRepository.findAll();

        for (Area area : areas) {
            Map<String, String> addParameter = new HashMap<>();
            addParameter.put("areaCode", area.getCode());

            List<NameCodeDto> response = openApiHelper.connectOpenApi("areaCode1", typeReference, addParameter);

            for (NameCodeDto nameCodeDto : response) {
                AreaDetail areadetail = AreaDetail.builder()
                        .area(area)
                        .name(nameCodeDto.getName())
                        .code(nameCodeDto.getCode())
                        .build();
                areaDetailRepository.save(areadetail);
            }
        }

    }

    public void saveContentTypes() {

        // OpenAPI 호출로 대분류 데이터를 가져옴
        List<NameCodeDto> MajorCats = openApiHelper.connectOpenApi("categoryCode1", typeReference, null);

        // 대분류 데이터를 ContentType 엔티티로 변환하여 저장
        for (NameCodeDto nameCodeDto1 : MajorCats) {
            ContentType MajorType1 = ContentType.builder()
                    .parentCode(null)
                    .code(nameCodeDto1.getCode())
                    .name(nameCodeDto1.getName())
                    .build();
            contentTypeRepository.save(MajorType1);
        }

        for (NameCodeDto nameCodeDto1 : MajorCats) {
            Map<String, String> addParameter = new HashMap<>();
            addParameter.put("cat1", nameCodeDto1.getCode());

            // OpenAPI 호출로 중분류 데이터를 가져옴
            List<NameCodeDto> MediumCats = openApiHelper.connectOpenApi("categoryCode1", typeReference, addParameter);

            // 중분류 데이터를 ContentType 엔티티로 변환하여 저장
            for (NameCodeDto nameCodeDto2 : MediumCats) {
                ContentType MediumType = ContentType.builder()
                        .parentCode(nameCodeDto1.getCode())
                        .code(nameCodeDto2.getCode())
                        .name(nameCodeDto2.getName())
                        .build();
                contentTypeRepository.save(MediumType);

                addParameter.put("cat2", nameCodeDto2.getCode());
                // OpenAPI 호출로 소분류 데이터를 가져옴
                List<NameCodeDto> MinorCats = openApiHelper.connectOpenApi("categoryCode1", typeReference, addParameter);

                // 소분류 데이터를 ContentType 엔티티로 변환하여 저장
                for (NameCodeDto nameCodeDto3 : MinorCats) {
                    ContentType MinorType = ContentType.builder()
                            .parentCode(nameCodeDto2.getCode())
                            .code(nameCodeDto3.getCode())
                            .name(nameCodeDto3.getName())
                            .build();
                    contentTypeRepository.save(MinorType);
                }
            }
        }
    }

    public void saveContents() {

        ParameterizedTypeReference<WrapperDto<ContentDto>> contentTypeReference
                = new ParameterizedTypeReference<>() {};
        List<ContentDto> response = openApiHelper.connectOpenApi(
                "areaBasedList1", contentTypeReference, null);

        for (ContentDto contentDto : response) {
            ContentType contentType = contentTypeRepository.findByCode(contentDto.getCat3());
            AreaDetail areaDetail = areaDetailRepository.findByAreaCodeAndCode(contentDto.getAreacode(), contentDto.getSigungucode());

            if (contentType == null || areaDetail == null) continue;

            Content content = Content.of(contentDto, contentType, areaDetail);
            contentRepository.save(content);
        }
    }
}
