package com.mono.trigo.web.content.dto;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.domain.content.entity.ContentCount;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {

    private Long id;
    private String contentType;
    private ContentAreaDetailResponse contentAreaDetailResponse;
    private String title;
    private Double mapX;
    private Double mapY;
    private String addr1;
    private String addr2;
    private String tel;
    private String firstImage;
    private String firstImage2;
    private ContentCount contentCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static ContentResponse of(Content content) {
        return builder()
                .id(content.getId())
                .contentType(content.getContentType().getName())
                .contentAreaDetailResponse(ContentAreaDetailResponse.of(content.getAreaDetail()))
                .title(content.getTitle())
                .mapX(content.getMapX())
                .mapY(content.getMapY())
                .addr1(content.getAddr1())
                .addr2(content.getAddr2())
                .tel(content.getTel())
                .firstImage(content.getFirstImage())
                .firstImage2(content.getFirstImage2())
                .contentCount(content.getContentCount())
                .createdDate(content.getCreatedDate())
                .modifiedDate(content.getModifiedDate())
                .build();
    }
}
