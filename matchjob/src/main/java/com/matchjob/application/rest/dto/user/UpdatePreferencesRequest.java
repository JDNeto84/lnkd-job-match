package com.matchjob.application.rest.dto.user;

public record UpdatePreferencesRequest(
        String keyword,
        String location,
        Boolean remotePreferred
) {
}
