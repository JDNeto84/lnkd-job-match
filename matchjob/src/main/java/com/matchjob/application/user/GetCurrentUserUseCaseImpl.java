package com.matchjob.application.user;

import java.util.Optional;

import com.matchjob.domain.user.User;
import com.matchjob.domain.valueobject.Email;
import com.matchjob.application.port.incoming.user.GetCurrentUserUseCase;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.dto.GetCurrentUserQuery;

public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> execute(GetCurrentUserQuery query) {
        Email emailVo = new Email(query.email());
        return userRepository.findByEmail(emailVo.value());
    }
}
