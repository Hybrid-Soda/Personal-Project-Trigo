package com.mono.trigo.web.exception.entity;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationError implements ErrorCode {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
