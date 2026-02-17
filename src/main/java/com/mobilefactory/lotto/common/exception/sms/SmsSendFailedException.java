package com.mobilefactory.lotto.common.exception.sms;

public class SmsSendFailedException extends RuntimeException {
    public SmsSendFailedException(String message) {
        super(message);
    }
}
