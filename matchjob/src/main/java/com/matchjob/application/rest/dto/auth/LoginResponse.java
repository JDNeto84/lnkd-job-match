package com.matchjob.application.rest.dto.auth;

public record LoginResponse(String token, String type, Long expiresIn) {
}
