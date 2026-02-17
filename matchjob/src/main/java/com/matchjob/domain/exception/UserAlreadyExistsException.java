package com.matchjob.domain.exception;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
