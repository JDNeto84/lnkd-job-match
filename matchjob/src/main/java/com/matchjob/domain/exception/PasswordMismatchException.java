package com.matchjob.domain.exception;

public class PasswordMismatchException extends DomainException {

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
