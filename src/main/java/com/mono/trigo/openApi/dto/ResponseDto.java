package com.mono.trigo.openApi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private HeaderDto header;
    private BodyDto body;

    @Builder
    public ResponseDto(HeaderDto header, BodyDto body) {
        this.header = header;
        this.body = body;
    }
}
