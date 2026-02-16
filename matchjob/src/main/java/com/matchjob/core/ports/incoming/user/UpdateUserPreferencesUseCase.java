package com.matchjob.core.ports.incoming.user;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.usecases.user.dto.UpdateUserPreferencesCommand;

public interface UpdateUserPreferencesUseCase {
    User execute(UpdateUserPreferencesCommand command);
}
