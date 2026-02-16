package com.matchjob.core.domain.exception;

public class WeakPasswordException extends DomainException {

    public WeakPasswordException(String message) {
        super(message);
    }

    public WeakPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
