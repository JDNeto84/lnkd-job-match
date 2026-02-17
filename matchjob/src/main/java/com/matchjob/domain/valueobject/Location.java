package com.matchjob.domain.valueobject;

import java.util.ArrayList;
import java.util.List;

import com.matchjob.domain.exception.InvalidLocationException;

public record Location(String value) {

    public Location {
        if (value == null || value.isBlank()) {
            throw new InvalidLocationException("Localização não pode ser nula ou vazia");
        }
        String canonical = canonicalize(value);
        if (canonical.length() < 2) {
            throw new InvalidLocationException("Localização deve ter pelo menos 2 caracteres");
        }
        if (canonical.length() > 100) {
            throw new InvalidLocationException("Localização deve ter no máximo 100 caracteres");
        }
        value = canonical;
    }

    private static String canonicalize(String input) {
        String cleaned = input.trim().replaceAll("\\s+", " ");
        String[] tokens = cleaned.split(",");
        List<String> parts = new ArrayList<>();
        for (String t : tokens) {
            String s = t.trim();
            if (!s.isEmpty()) {
                parts.add(s);
            }
        }
        if (parts.isEmpty()) {
            throw new InvalidLocationException("Formato de localização inválido");
        }
        for (String p : parts) {
            if (p.length() < 2) {
                throw new InvalidLocationException("Cada parte da localização deve ter pelo menos 2 caracteres");
            }
        }
        if (parts.size() == 1) {
            String p0 = parts.get(0);
            if (isBrasil(p0)) {
                return "Brasil";
            }
            return p0;
        }
        if (parts.size() == 2) {
            String p0 = parts.get(0);
            String p1 = parts.get(1);
            if (isBrasil(p1)) {
                return p0 + ", Brasil";
            }
            return p0 + ", " + p1 + ", Brasil";
        }
        if (parts.size() == 3) {
            String p2 = parts.get(2);
            String country = p2.isBlank() ? "Brasil" : p2;
            return parts.get(0) + ", " + parts.get(1) + ", " + country;
        }
        throw new InvalidLocationException("Formato de localização inválido");
    }

    private static boolean isBrasil(String s) {
        return "Brasil".equalsIgnoreCase(s);
    }

    @Override
    public String toString() {
        return value;
    }
}
