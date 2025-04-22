package com.mono.trigo.web.content.dto;

import com.mono.trigo.domain.area.entity.AreaDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContentAreaDetailResponse {

    private String areaName;
    private String areaDetailName;

    public static ContentAreaDetailResponse of(AreaDetail areaDetail) {
        return builder()
                .areaName(areaDetail.getArea().getName())
                .areaDetailName(areaDetail.getName())
                .build();
    }
}
