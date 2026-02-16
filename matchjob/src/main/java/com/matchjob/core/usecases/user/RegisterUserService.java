package com.matchjob.core.usecases.user;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.entity.UserPlan;
import com.matchjob.core.domain.entity.UserRole;
import com.matchjob.core.domain.exception.UserAlreadyExistsException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.domain.valueobject.Email;
import com.matchjob.core.domain.valueobject.Password;
import com.matchjob.core.ports.outgoing.PasswordEncoder;
import com.matchjob.core.ports.incoming.user.RegisterUserUseCase;
import com.matchjob.core.usecases.user.dto.RegisterUserCommand;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(RegisterUserCommand command) {
        Email emailVo = new Email(command.email());
        var existing = userRepository.findByEmail(emailVo.value());
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException("Email já cadastrado");
        }

        Password password = Password.create(command.password(), passwordEncoder);

        User user = User.createUser(
                command.name(),
                emailVo.value(),
                password.value(),
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        return userRepository.save(user);
    }
}
