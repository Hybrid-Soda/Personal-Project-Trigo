package com.mono.trigo.web.content.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentSearchCondition {

    private String arrange;
    private String areaCode;
    private String areaDetailCode;
    private String contentTypeCode;
}
