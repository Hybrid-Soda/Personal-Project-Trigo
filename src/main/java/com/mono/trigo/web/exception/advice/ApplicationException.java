package com.mono.trigo.web.exception.advice;

import com.mono.trigo.web.exception.entity.ApplicationError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ApplicationError error;

    public ApplicationException(Throwable cause, ApplicationError error) {
        super(cause);
        this.error = error;
    }
}
