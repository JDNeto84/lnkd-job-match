package com.matchjob.application.user;

import com.matchjob.domain.exception.UserAlreadyExistsException;
import com.matchjob.domain.user.User;
import com.matchjob.domain.user.UserPlan;
import com.matchjob.domain.user.UserRole;
import com.matchjob.domain.valueobject.Email;
import com.matchjob.application.port.incoming.user.RegisterUserUseCase;
import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.dto.RegisterUserCommand;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

        User user = User.createNewUser(
                command.name(),
                emailVo.value(),
                command.password(),
                UserPlan.FREE,
                UserRole.USER,
                passwordEncoder
        );

        return userRepository.save(user);
    }
}
