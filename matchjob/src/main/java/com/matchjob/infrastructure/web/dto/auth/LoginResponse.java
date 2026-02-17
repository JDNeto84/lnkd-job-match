package com.matchjob.infrastructure.web.dto.auth;

public record LoginResponse(String token, String type, Long expiresIn) {
}

