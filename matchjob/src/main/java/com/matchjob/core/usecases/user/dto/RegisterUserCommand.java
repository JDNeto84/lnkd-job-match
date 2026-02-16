package com.matchjob.core.usecases.user.dto;

public record RegisterUserCommand(String name, String email, String password) {
}
