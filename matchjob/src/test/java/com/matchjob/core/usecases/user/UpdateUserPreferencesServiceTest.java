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
import com.matchjob.core.domain.exception.UserNotFoundException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.usecases.user.dto.UpdateUserPreferencesCommand;

class UpdateUserPreferencesServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserPreferencesService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAtualizarPreferenciasComSucesso() {
        User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                null,
                null,
                true
        );
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UpdateUserPreferencesCommand command = new UpdateUserPreferencesCommand(
                userId,
                "product manager",
                "São Paulo, SP",
                true
        );

        User result = service.execute(command);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                null,
                null,
                true
        );
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UpdateUserPreferencesCommand command = new UpdateUserPreferencesCommand(
                userId,
                "product manager",
                "São Paulo, SP",
                true
        );

        assertThrows(UserNotFoundException.class, () -> service.execute(command));
        verify(userRepository).findById(userId);
    }
}
