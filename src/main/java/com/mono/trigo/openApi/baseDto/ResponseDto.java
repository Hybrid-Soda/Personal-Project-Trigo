package com.mono.trigo.openApi.baseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<T> {

    private HeaderDto header;
    private BodyDto<T> body;

    @Builder
    public ResponseDto(HeaderDto header, BodyDto<T> body) {
        this.header = header;
        this.body = body;
    }
}
