package com.mobilefactory.lotto.common.exception.auth;

public class InvalidAuthCodeException extends RuntimeException {
    public InvalidAuthCodeException(String message) {
        super(message);
    }
}
