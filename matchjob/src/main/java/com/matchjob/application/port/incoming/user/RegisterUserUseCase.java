package com.matchjob.application.port.incoming.user;

import com.matchjob.application.user.dto.RegisterUserCommand;
import com.matchjob.domain.user.User;

public interface RegisterUserUseCase {
    User execute(RegisterUserCommand command);
}
