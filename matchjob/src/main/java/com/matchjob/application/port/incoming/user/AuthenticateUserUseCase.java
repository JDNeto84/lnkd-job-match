package com.matchjob.application.port.incoming.user;

import com.matchjob.application.user.dto.AuthenticateUserCommand;
import com.matchjob.application.user.dto.AuthenticateUserResult;

public interface AuthenticateUserUseCase {
    AuthenticateUserResult execute(AuthenticateUserCommand command);
}
