package com.matchjob.domain.valueobject;

import java.util.regex.Pattern;

import com.matchjob.domain.exception.InvalidEmailException;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email não pode ser nulo ou vazio");
        }
        String normalized = value.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidEmailException("Formato de email inválido");
        }
        value = normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}
