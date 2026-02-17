package com.matchjob.core.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.matchjob.domain.exception.InvalidLocationException;
import com.matchjob.domain.valueobject.Location;

class LocationTest {

    @Test
    void deveCriarLocalizacaoCidadeEstado() {
        Location location = new Location("  São Paulo  ,  SP ");
        assertEquals("São Paulo, SP, Brasil", location.value());
    }

    @Test
    void deveFalharQuandoLocalizacaoForNulaOuVazia() {
        assertThrows(InvalidLocationException.class, () -> new Location(null));
        assertThrows(InvalidLocationException.class, () -> new Location("   "));
    }

    @Test
    void deveFalharQuandoLocalizacaoForMuitoCurta() {
        assertThrows(InvalidLocationException.class, () -> new Location("A"));
    }

    @Test
    void deveFalharQuandoLocalizacaoForMuitoLonga() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append("a");
        }
        String longoDemais = builder.toString();
        assertThrows(InvalidLocationException.class, () -> new Location(longoDemais));
    }

    @Test
    void deveCriarCidadeSomenteComPaisDefault() {
        Location location = new Location("Curitiba");
        assertEquals("Curitiba", location.value());
    }

    @Test
    void deveCriarEstadoSomenteComPaisDefault() {
        Location location = new Location("SP");
        assertEquals("SP", location.value());
    }

    @Test
    void deveCriarEstadoComPaisBrasil() {
        Location location = new Location("SP, Brasil");
        assertEquals("SP, Brasil", location.value());
    }

    @Test
    void deveCriarCidadeEstadoPaisExplicito() {
        Location location = new Location("Lisboa, Lisboa, Portugal");
        assertEquals("Lisboa, Lisboa, Portugal", location.value());
    }

    @Test
    void deveCriarBrasilSomente() {
        Location location = new Location("Brasil");
        assertEquals("Brasil", location.value());
    }
}
