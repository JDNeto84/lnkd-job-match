package com.matchjob.core.ports.incoming.user;

import java.util.Optional;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.usecases.user.dto.GetCurrentUserQuery;

public interface GetCurrentUserUseCase {
    Optional<User> execute(GetCurrentUserQuery query);
}
