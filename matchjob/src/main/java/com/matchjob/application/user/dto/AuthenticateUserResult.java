package com.matchjob.application.user.dto;

public record AuthenticateUserResult(String token, String tokenType, long expiresInSeconds) {
}

