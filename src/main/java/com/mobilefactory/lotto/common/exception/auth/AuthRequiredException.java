package com.mobilefactory.lotto.common.exception.auth;

public class AuthRequiredException extends RuntimeException {
    public AuthRequiredException(String message) {
        super(message);
    }
}
