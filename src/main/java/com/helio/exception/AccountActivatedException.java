package com.helio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountActivatedException extends RuntimeException {
    public AccountActivatedException(String message) {
        super(message);
    }
}
