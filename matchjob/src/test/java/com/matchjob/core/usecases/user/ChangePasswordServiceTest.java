package com.matchjob.core.usecases.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.exception.PasswordMismatchException;
import com.matchjob.core.domain.exception.UserNotFoundException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.ports.outgoing.PasswordEncoder;
import com.matchjob.core.usecases.user.dto.ChangePasswordCommand;

class ChangePasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ChangePasswordService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAlterarSenhaComSucesso() {
        String hash = "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789";
        User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                hash,
                null,
                null,
                true
        );
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("SenhaAtual123", hash)).thenReturn(true);
        when(passwordEncoder.encode("NovaSenha123")).thenReturn("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ChangePasswordCommand command = new ChangePasswordCommand(userId, "SenhaAtual123", "NovaSenha123");

        User result = service.execute(command);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        String hash = "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789";
        User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                hash,
                null,
                null,
                true
        );
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ChangePasswordCommand command = new ChangePasswordCommand(userId, "SenhaAtual123", "NovaSenha123");

        assertThrows(UserNotFoundException.class, () -> service.execute(command));
        verify(userRepository).findById(userId);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaAtualInvalida() {
        String hash = "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789";
        User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                hash,
                null,
                null,
                true
        );
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("SenhaAtualErrada", hash)).thenReturn(false);

        ChangePasswordCommand command = new ChangePasswordCommand(userId, "SenhaAtualErrada", "NovaSenha123");

        assertThrows(PasswordMismatchException.class, () -> service.execute(command));
    }
}
