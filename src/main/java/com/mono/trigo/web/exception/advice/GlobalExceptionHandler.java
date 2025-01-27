package com.mono.trigo.web.exception.advice;

import com.mono.trigo.web.exception.dto.ErrorResponse;
import com.mono.trigo.web.exception.dto.ValidErrorResponse;
import com.mono.trigo.web.exception.entity.ApplicationError;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.status(400).body(ValidErrorResponse.of(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        ApplicationError error = exception.getError();

        return ResponseEntity.status(error.getHttpStatus()).body(ErrorResponse.of(error));
    }
}
