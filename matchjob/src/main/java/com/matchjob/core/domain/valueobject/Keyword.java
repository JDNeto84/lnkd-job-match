package com.matchjob.core.domain.valueobject;

import com.matchjob.core.domain.exception.InvalidKeywordException;

public record Keyword(String value) {

    public Keyword {
        if (value == null || value.isBlank()) {
            throw new InvalidKeywordException("Keyword não pode ser nula ou vazia");
        }
        String normalized = normalize(value);
        if (normalized.length() < 2) {
            throw new InvalidKeywordException("Keyword deve ter pelo menos 2 caracteres");
        }
        if (normalized.length() > 100) {
            throw new InvalidKeywordException("Keyword deve ter no máximo 100 caracteres");
        }
        value = normalized;
    }

    private static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    @Override
    public String toString() {
        return value;
    }
}
