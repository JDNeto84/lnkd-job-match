package com.matchjob.core.usecases.user.dto;

import java.util.UUID;

public record ChangePasswordCommand(
        UUID userId,
        String currentPassword,
        String newPassword
) {
}
