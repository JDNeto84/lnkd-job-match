package com.matchjob.application.port.incoming.user;

import java.util.Optional;

import com.matchjob.application.user.dto.GetCurrentUserQuery;
import com.matchjob.domain.user.User;

public interface GetCurrentUserUseCase {
    Optional<User> execute(GetCurrentUserQuery query);
}
