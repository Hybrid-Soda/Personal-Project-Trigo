package com.mono.trigo.web.exception.entity;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    String getMessage();
    Integer getCode();
}
