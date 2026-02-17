package com.matchjob.infrastructure.web.handler.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        int status,
        LocalDateTime timestamp
) {
}

