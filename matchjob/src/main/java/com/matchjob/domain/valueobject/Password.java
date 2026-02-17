package com.matchjob.domain.valueobject;

import java.util.regex.Pattern;

import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.domain.exception.WeakPasswordException;

public record Password(String value) {
    private static final Pattern HASH_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");

    public Password {
        if (value == null || value.isBlank()) {
            throw new WeakPasswordException("Senha não pode ser nula ou vazia");
        }
        if (!looksLikeHash(value)) {
            validateRawPassword(value);
        }
    }

    public static Password create(String rawPassword, PasswordEncoder encoder) {
        validateRawPassword(rawPassword);
        return new Password(encoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, value);
    }

    public static Password fromHashed(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new WeakPasswordException("Hash de senha não pode ser nulo ou vazio");
        }

        if (!isValidHash(hashedPassword)) {
            throw new WeakPasswordException("Formato de hash de senha inválido");
        }

        return new Password(hashedPassword);
    }

    private static void validateRawPassword(String password) {
        if (password.length() < 8) {
            throw new WeakPasswordException("Senha deve ter pelo menos 8 caracteres");
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            throw new WeakPasswordException("Senha deve conter letras maiúsculas, minúsculas e números");
        }
    }

    private static boolean looksLikeHash(String password) {
        return password.length() >= 60 &&
                (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    private static boolean isValidHash(String password) {
        return HASH_PATTERN.matcher(password).matches();
    }

    @Override
    public String toString() {
        return "[PROTEGIDO]";
    }
}
