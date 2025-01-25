package com.mono.trigo.web.exception.advice;

import com.mono.trigo.web.exception.dto.ErrorResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        ApplicationError error = exception.getError();

        return ResponseEntity.status(error.getHttpStatus()).body(ErrorResponse.of(error));
    }
}
