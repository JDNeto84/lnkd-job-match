package com.matchjob.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.matchjob.core.domain.exception.InvalidEmailException;

class EmailTest {

    @Test
    void deveCriarEmailValido() {
        Email email = new Email("usuario@dominio.com");
        assertEquals("usuario@dominio.com", email.value());
    }

    @Test
    void deveLancarExcecaoParaEmailInvalido() {
        assertThrows(InvalidEmailException.class, () -> new Email("invalido"));
    }

    @Test
    void deveLancarExcecaoParaEmailVazio() {
        assertThrows(InvalidEmailException.class, () -> new Email(" "));
    }
}
