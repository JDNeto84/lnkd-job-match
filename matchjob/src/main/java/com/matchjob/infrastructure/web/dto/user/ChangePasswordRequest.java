package com.matchjob.infrastructure.web.dto.user;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}

