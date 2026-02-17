package com.matchjob.infrastructure.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.matchjob.domain.exception.UserAlreadyExistsException;
import com.matchjob.domain.user.User;
import com.matchjob.domain.user.UserPlan;
import com.matchjob.domain.user.UserRole;
import com.matchjob.domain.valueobject.Email;
import com.matchjob.domain.valueobject.Name;
import com.matchjob.domain.valueobject.Password;
import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.dto.RegisterUserCommand;

class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private com.matchjob.application.user.RegisterUserUseCaseImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRegistrarUsuarioQuandoEmailNaoExiste() {
        RegisterUserCommand command = new RegisterUserCommand("João", "joao@teste.com", "Senha123");
        when(userRepository.findByEmail("joao@teste.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Senha123")).thenReturn("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789");

        User saved = User.reconstructUser(
                java.util.UUID.randomUUID(),
                new Name("João"),
                new Email("joao@teste.com"),
                Password.fromHashed("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789"),
                UserPlan.FREE,
                UserRole.USER,
                true,
                null,
                null,
                false,
                null,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = service.execute(command);

        assertNotNull(result);
        verify(userRepository).findByEmail("joao@teste.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        RegisterUserCommand command = new RegisterUserCommand("João", "joao@teste.com", "Senha123");
        User existente = User.reconstructUser(
                java.util.UUID.randomUUID(),
                new Name("João"),
                new Email("joao@teste.com"),
                Password.fromHashed("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789"),
                UserPlan.FREE,
                UserRole.USER,
                true,
                null,
                null,
                false,
                null,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        when(userRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(existente));

        assertThrows(UserAlreadyExistsException.class, () -> service.execute(command));
        verify(userRepository).findByEmail("joao@teste.com");
    }
}
