package com.matchjob.core.ports.incoming.user;

import com.matchjob.core.usecases.user.dto.AuthenticateUserCommand;
import com.matchjob.core.usecases.user.dto.AuthenticateUserResult;

public interface AuthenticateUserUseCase {
    AuthenticateUserResult execute(AuthenticateUserCommand command);
}
