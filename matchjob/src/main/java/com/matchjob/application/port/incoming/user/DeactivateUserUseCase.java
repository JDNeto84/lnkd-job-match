package com.matchjob.application.port.incoming.user;

import java.util.UUID;

public interface DeactivateUserUseCase {
    void execute(UUID userId);
}
