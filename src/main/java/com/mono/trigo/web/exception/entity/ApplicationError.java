package com.mono.trigo.web.exception.entity;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationError implements ErrorCode {

    // user
    USERNAME_IS_EXISTED(HttpStatus.CONFLICT, "해당 아이디를 사용할 수 없습니다.", HttpStatus.CONFLICT.value()),
    USERID_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 아이디를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    INPUT_DATA_IS_INVALID(HttpStatus.BAD_REQUEST, "입력 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value());

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
