package com.matchjob.application.rest.dto.user;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
