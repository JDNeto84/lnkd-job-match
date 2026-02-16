package com.matchjob.core.usecases.user;

import java.util.UUID;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.exception.PasswordMismatchException;
import com.matchjob.core.domain.exception.UserNotFoundException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.domain.valueobject.Password;
import com.matchjob.core.ports.incoming.user.ChangePasswordUseCase;
import com.matchjob.core.ports.outgoing.PasswordEncoder;
import com.matchjob.core.usecases.user.dto.ChangePasswordCommand;

public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

        Password currentHashed = Password.fromHashed(current.getPassword());
        if (!currentHashed.matches(command.currentPassword(), passwordEncoder)) {
            throw new PasswordMismatchException("Senha atual inválida");
        }

        Password newPassword = Password.create(command.newPassword(), passwordEncoder);

        User updated = current.withPassword(newPassword.value());

        return userRepository.save(updated);
    }
}
