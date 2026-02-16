package com.matchjob.application.rest.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.matchjob.application.rest.handler.response.ErrorResponse;
import com.matchjob.core.domain.exception.PasswordMismatchException;

class GlobalExceptionHandlerTest {

    @Test
    void deveRetornar400QuandoSenhaAtualInvalida() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        PasswordMismatchException ex = new PasswordMismatchException("Senha atual inválida");

        ResponseEntity<ErrorResponse> response = handler.handlePasswordMismatch(ex);

        assertEquals(400, response.getStatusCode().value());
        ErrorResponse body = response.getBody();
        assertEquals("password_mismatch", body.code());
        assertEquals("Senha atual inválida", body.message());
    }
}

