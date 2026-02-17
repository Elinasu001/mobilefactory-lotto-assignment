package com.mobilefactory.lotto.common.exception.event;

public class ExceedMaxParticipantsException extends RuntimeException {
    public ExceedMaxParticipantsException(String message) {
        super(message);
    }
}
