package com.matchjob.core.usecases.user;

import java.util.Optional;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.ports.incoming.user.GetCurrentUserUseCase;
import com.matchjob.core.usecases.user.dto.GetCurrentUserQuery;

public class GetCurrentUserService implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> execute(GetCurrentUserQuery query) {
        com.matchjob.core.domain.valueobject.Email emailVo = new com.matchjob.core.domain.valueobject.Email(query.email());
        return userRepository.findByEmail(emailVo.value());
    }
}
