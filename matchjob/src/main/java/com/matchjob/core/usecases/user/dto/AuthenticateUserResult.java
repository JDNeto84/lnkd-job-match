package com.matchjob.core.usecases.user.dto;

public record AuthenticateUserResult(String token, String tokenType, long expiresInSeconds) {
}
