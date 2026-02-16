package com.matchjob.application.rest.dto.auth;

import java.util.UUID;

public record MeResponse(UUID id, String name, String email, String role, String plan) {
}
