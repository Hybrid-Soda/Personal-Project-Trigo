package com.mono.trigo.web.exception.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ValidErrorResponse(HttpStatus httpStatus, List<String> message, Integer code) {

    public static ValidErrorResponse of(List<String> messageList) {
        return new ValidErrorResponse(HttpStatus.BAD_REQUEST, messageList, 400);
    }
}
