package com.mono.trigo.openApi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BodyDto {

    private ItemsDto items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;

    @Builder
    public BodyDto(ItemsDto items, Integer numOfRows, Integer pageNo, Integer totalCount) {
        this.items = items;
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
    }
}
