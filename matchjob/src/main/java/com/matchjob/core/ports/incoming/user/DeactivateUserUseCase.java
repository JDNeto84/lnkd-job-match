package com.matchjob.core.ports.incoming.user;

import java.util.UUID;

public interface DeactivateUserUseCase {
    void execute(UUID userId);
}
