package com.mono.trigo.openApi.baseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class HeaderDto {

    private String resultCode;
    private String resultMsg;

    @Builder
    public HeaderDto(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
