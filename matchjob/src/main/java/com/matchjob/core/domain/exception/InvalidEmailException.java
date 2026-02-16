package com.matchjob.core.domain.exception;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
