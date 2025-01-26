package com.mono.trigo.web.exception.dto;

import com.mono.trigo.web.exception.entity.ApplicationError;
import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus httpStatus, String message, Integer code) {

    public static ErrorResponse of(ApplicationError error) {
        return new ErrorResponse(error.getHttpStatus(), error.getMessage(), error.getCode());
    }
}
