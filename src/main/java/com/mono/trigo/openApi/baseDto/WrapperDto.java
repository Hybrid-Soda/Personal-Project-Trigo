package com.mono.trigo.openApi.baseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class WrapperDto<T> {

    private ResponseDto<T> response;

    @Builder
    public WrapperDto(ResponseDto<T> response) {
        this.response = response;
    }
}
