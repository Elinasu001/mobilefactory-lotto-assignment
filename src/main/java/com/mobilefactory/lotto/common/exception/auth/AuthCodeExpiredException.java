package com.mobilefactory.lotto.common.exception.auth;

public class AuthCodeExpiredException extends RuntimeException {
    public AuthCodeExpiredException(String message) {
        super(message);
    }
}
