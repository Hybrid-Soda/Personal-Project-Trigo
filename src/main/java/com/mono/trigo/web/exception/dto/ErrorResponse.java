package com.mono.trigo.web.exception.dto;

import com.mono.trigo.web.exception.entity.ApplicationError;

public record ErrorResponse(String message) {

    public static ErrorResponse of(ApplicationError error) {
        return new ErrorResponse(error.getMessage());
    }
}
