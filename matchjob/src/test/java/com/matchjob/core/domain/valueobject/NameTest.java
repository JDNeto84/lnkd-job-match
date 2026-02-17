package com.matchjob.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.matchjob.domain.exception.InvalidNameException;
import com.matchjob.domain.valueobject.Name;

class NameTest {

    @Test
    void deveCriarNomeValido() {
        Name name = new Name(" João da Silva ");
        assertEquals("João da Silva", name.value());
    }

    @Test
    void deveFalharQuandoNomeForNuloOuVazio() {
        assertThrows(InvalidNameException.class, () -> new Name(null));
        assertThrows(InvalidNameException.class, () -> new Name("   "));
    }

    @Test
    void deveFalharQuandoNomeForMuitoCurto() {
        assertThrows(InvalidNameException.class, () -> new Name("J"));
    }

    @Test
    void deveFalharQuandoNomeForMuitoLongo() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append("a");
        }
        String longoDemais = builder.toString();
        assertThrows(InvalidNameException.class, () -> new Name(longoDemais));
    }

    @Test
    void deveFalharQuandoNomeConterNumeros() {
        assertThrows(InvalidNameException.class, () -> new Name("João 2"));
    }
}

