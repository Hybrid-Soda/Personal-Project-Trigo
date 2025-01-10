package com.mono.trigo.openApi.baseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemsDto<T> {

    private List<T> item;

    @Builder
    public ItemsDto(List<T> item) {
        this.item = item;
    }
}


