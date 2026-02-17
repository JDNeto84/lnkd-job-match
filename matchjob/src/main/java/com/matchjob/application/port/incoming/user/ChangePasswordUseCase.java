package com.matchjob.application.port.incoming.user;

import com.matchjob.application.user.dto.ChangePasswordCommand;
import com.matchjob.domain.user.User;

public interface ChangePasswordUseCase {
    User execute(ChangePasswordCommand command);
}
