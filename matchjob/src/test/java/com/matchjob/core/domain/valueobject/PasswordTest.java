package com.matchjob.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.matchjob.core.domain.exception.WeakPasswordException;
import com.matchjob.core.ports.outgoing.PasswordEncoder;

class PasswordTest {

    @Test
    void deveCriarSenhaCriptografadaComEncoder() {
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode("Senha123")).thenReturn("$2a$10$abcdefghijklmnopqrstuvxyz0123456789ABCDEFXYZabcde");

        Password password = Password.create("Senha123", encoder);

        assertTrue(password.value().startsWith("$2a$10$"));
    }

    @Test
    void deveLancarExcecaoParaSenhaFraca() {
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        assertThrows(WeakPasswordException.class, () -> Password.create("abc", encoder));
    }

    @Test
    void deveCriarAPartirDeHashValido() {
        String hash = "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789";
        Password password = Password.fromHashed(hash);
        assertEquals(hash, password.value());
    }

    @Test
    void toStringNaoDeveExporSenha() {
        Password password = new Password("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789");
        assertEquals("[PROTEGIDO]", password.toString());
    }
}
