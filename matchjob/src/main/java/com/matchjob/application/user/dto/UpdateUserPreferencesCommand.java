package com.matchjob.application.user.dto;

import java.util.UUID;

public record UpdateUserPreferencesCommand(
        UUID userId,
        String keyword,
        String location,
        Boolean remotePreferred
) {
}

