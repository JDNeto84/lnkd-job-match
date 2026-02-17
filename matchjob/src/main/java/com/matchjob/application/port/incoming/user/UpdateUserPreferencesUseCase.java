package com.matchjob.application.port.incoming.user;

import com.matchjob.application.user.dto.UpdateUserPreferencesCommand;
import com.matchjob.domain.user.User;

public interface UpdateUserPreferencesUseCase {
    User execute(UpdateUserPreferencesCommand command);
}
