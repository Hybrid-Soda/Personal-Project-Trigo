package com.mono.trigo.openApi.baseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BodyDto<T> {

    private ItemsDto<T> items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;

    @Builder
    public BodyDto(ItemsDto<T> items, Integer numOfRows, Integer pageNo, Integer totalCount) {
        this.items = items;
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
    }
}
