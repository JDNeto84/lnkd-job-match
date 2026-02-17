package com.matchjob.infrastructure.web.dto.user;

public record UpdatePreferencesRequest(
        String keyword,
        String location,
        Boolean remotePreferred
) {
}

