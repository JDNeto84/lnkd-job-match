package com.matchjob.application.user;

import java.util.UUID;

import com.matchjob.domain.exception.PasswordMismatchException;
import com.matchjob.domain.exception.UserNotFoundException;
import com.matchjob.domain.user.User;
import com.matchjob.domain.valueobject.Password;
import com.matchjob.application.port.incoming.user.ChangePasswordUseCase;
import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.dto.ChangePasswordCommand;

public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(ChangePasswordCommand command) {
        UUID userId = command.userId();
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        User current = userOpt.get();

        Password currentHashed = current.getPasswordVo();
        if (!currentHashed.matches(command.currentPassword(), passwordEncoder)) {
            throw new PasswordMismatchException("Senha atual inválida");
        }

        Password newPassword = Password.create(command.newPassword(), passwordEncoder);

        User updated = current.withPassword(newPassword);

        return userRepository.save(updated);
    }
}
