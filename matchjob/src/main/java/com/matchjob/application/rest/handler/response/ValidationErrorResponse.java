package com.matchjob.application.rest.handler.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        String code,
        String message,
        int status,
        Map<String, String> errors,
        LocalDateTime timestamp
) {
}
