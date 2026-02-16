package com.matchjob.core.usecases.user.dto;

import java.util.UUID;

public record UpdateUserPreferencesCommand(
        UUID userId,
        String keyword,
        String location,
        Boolean remotePreferred
) {
}
