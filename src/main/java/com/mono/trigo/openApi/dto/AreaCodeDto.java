package com.mono.trigo.openApi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AreaCodeDto {

    private Integer rnum;
    private String code;
    private String name;

    @Builder
    public AreaCodeDto(Integer rnum, String code , String name) {
        this.rnum = rnum;
        this.code = code;
        this.name = name;
    }
}