package com.matchjob.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.matchjob.core.domain.exception.InvalidKeywordException;

class KeywordTest {

    @Test
    void deveCriarKeywordValida() {
        Keyword keyword = new Keyword("  Desenvolvedor   Backend ");
        assertEquals("Desenvolvedor Backend", keyword.value());
    }

    @Test
    void deveFalharQuandoKeywordForNulaOuVazia() {
        assertThrows(InvalidKeywordException.class, () -> new Keyword(null));
        assertThrows(InvalidKeywordException.class, () -> new Keyword("   "));
    }

    @Test
    void deveFalharQuandoKeywordForMuitoCurta() {
        assertThrows(InvalidKeywordException.class, () -> new Keyword("D"));
    }

    @Test
    void deveFalharQuandoKeywordForMuitoLonga() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append("a");
        }
        String longoDemais = builder.toString();
        assertThrows(InvalidKeywordException.class, () -> new Keyword(longoDemais));
    }

    @Test
    void deveAceitarNumerosECaracteresEspeciais() {
        assertDoesNotThrow(() -> new Keyword("Product Manager Jr (100% remoto)"));
        Keyword keyword = new Keyword("Product Manager Jr (100% remoto)");
        assertEquals("Product Manager Jr (100% remoto)", keyword.value());
    }
}
