package com.matchjob.core.usecases.user.dto;

public record AuthenticateUserCommand(String email, String password) {
}
