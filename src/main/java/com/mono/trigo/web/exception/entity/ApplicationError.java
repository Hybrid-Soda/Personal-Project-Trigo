package com.mono.trigo.web.exception.entity;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationError implements ErrorCode {

    // common
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED.value()),
    UNRECOGNIZED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),

    // user
    USER_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    USERNAME_IS_EXISTED(HttpStatus.CONFLICT, "해당 아이디를 사용할 수 없습니다.", HttpStatus.CONFLICT.value()),
    NICKNAME_IS_EXISTED(HttpStatus.CONFLICT, "해당 닉네임을 사용할 수 없습니다.", HttpStatus.CONFLICT.value()),

    // auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED.value()),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"액세스 토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED.value()),
    ACCESS_TOKEN_IS_INVALID(HttpStatus.UNAUTHORIZED,"액세스 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED.value()),
    INVALID_REQUEST_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED.value()),

    // plan
    PLAN_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    PLAN_ID_IS_INVALID(HttpStatus.BAD_REQUEST, "일정 아이디가 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    LIKE_IS_EXISTED(HttpStatus.CONFLICT, "이미 좋아요 되어있습니다.", HttpStatus.CONFLICT.value()),

    // review
    REVIEW_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    REVIEW_ID_IS_INVALID(HttpStatus.BAD_REQUEST, "리뷰 아이디가 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),

    // content & area
    CONTENT_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "컨텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    AREA_DETAIL_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "세부 장소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    CONTENT_ID_IS_INVALID(HttpStatus.BAD_REQUEST, "컨텐츠 아이디가 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
