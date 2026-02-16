package com.matchjob.application.rest.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.matchjob.application.rest.handler.response.ErrorResponse;
import com.matchjob.application.rest.handler.response.ValidationErrorResponse;
import com.matchjob.core.domain.exception.InvalidEmailException;
import com.matchjob.core.domain.exception.PasswordMismatchException;
import com.matchjob.core.domain.exception.UserAlreadyExistsException;
import com.matchjob.core.domain.exception.UserNotFoundException;
import com.matchjob.core.domain.exception.WeakPasswordException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmail(InvalidEmailException ex) {
        return buildErrorResponse(
                "invalid_email",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<ErrorResponse> handleWeakPassword(WeakPasswordException ex) {
        return buildErrorResponse(
                "password_weak",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatch(PasswordMismatchException ex) {
        return buildErrorResponse(
                "password_mismatch",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(
                "user_already_exists",
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(
                "user_not_found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse(
                "invalid_credentials",
                "Email ou senha inválidos",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildErrorResponse(
                "access_denied",
                "Você não tem permissão para acessar este recurso",
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "Campo inválido",
                        (existing, replacement) -> existing
                ));

        ValidationErrorResponse body = new ValidationErrorResponse(
                "validation_error",
                "Erro de validação nos campos",
                HttpStatus.BAD_REQUEST.value(),
                errors,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(
                "invalid_argument",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro interno não esperado", ex);

        return buildErrorResponse(
                "internal_error",
                "Ocorreu um erro interno. Tente novamente mais tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String code, String message, HttpStatus status) {
        ErrorResponse body = new ErrorResponse(
                code,
                message,
                status.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}
