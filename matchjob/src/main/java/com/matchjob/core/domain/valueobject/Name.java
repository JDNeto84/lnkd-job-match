package com.matchjob.core.domain.valueobject;

import com.matchjob.core.domain.exception.InvalidNameException;

public record Name(String value) {

    public Name {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException("Nome não pode ser nulo ou vazio");
        }
        String normalized = normalize(value);
        if (normalized.length() < 2) {
            throw new InvalidNameException("Nome deve ter pelo menos 2 caracteres");
        }
        if (normalized.length() > 100) {
            throw new InvalidNameException("Nome deve ter no máximo 100 caracteres");
        }
        if (containsDigit(normalized)) {
            throw new InvalidNameException("Nome não pode conter números");
        }
        value = normalized;
    }

    private static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    private static boolean containsDigit(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}

