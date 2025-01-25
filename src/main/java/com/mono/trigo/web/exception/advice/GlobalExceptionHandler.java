package com.mono.trigo.web.exception.advice;

import com.mono.trigo.web.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleApplicationException() {
        ErrorResponse errorResponse = new ErrorResponse();

        return ResponseEntity.status(404).body(errorResponse);
    }
}
