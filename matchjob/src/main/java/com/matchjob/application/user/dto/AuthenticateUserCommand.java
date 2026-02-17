package com.matchjob.application.user.dto;

public record AuthenticateUserCommand(String email, String password) {
}

