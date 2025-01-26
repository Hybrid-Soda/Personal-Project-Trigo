package com.mono.trigo.web.exception.entity;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationError implements ErrorCode {

    // common
    UNRECOGNIZED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // user
    USERNAME_IS_EXISTED(HttpStatus.CONFLICT, "해당 아이디를 사용할 수 없습니다.", HttpStatus.CONFLICT.value()),
    USER_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    INPUT_DATA_IS_INVALID(HttpStatus.BAD_REQUEST, "입력 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),

    // auth
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED,"권한이 없습니다.", HttpStatus.UNAUTHORIZED.value()),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"액세스 토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value()),
    ACCESS_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED,"액세스 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED.value());

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
