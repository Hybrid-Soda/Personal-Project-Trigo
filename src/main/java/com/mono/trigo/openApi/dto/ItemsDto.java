package com.mono.trigo.openApi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemsDto {

    private List<ItemDto> item;

    @Builder
    public ItemsDto(List<ItemDto> item) {
        this.item = item;
    }
}

@Getter
@Setter
@NoArgsConstructor
class ItemDto {

    private Integer rnum;
    private String code;
    private String name;

    @Builder
    public ItemDto(Integer rnum, String code , String name) {
        this.rnum = rnum;
        this.code = code;
        this.name = name;
    }
}
