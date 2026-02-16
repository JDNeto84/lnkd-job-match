package com.matchjob.core.usecases.user;

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

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.exception.UserAlreadyExistsException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.ports.outgoing.PasswordEncoder;
import com.matchjob.core.usecases.user.dto.RegisterUserCommand;

class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRegistrarUsuarioQuandoEmailNaoExiste() {
        RegisterUserCommand command = new RegisterUserCommand("João", "joao@teste.com", "Senha123");
        when(userRepository.findByEmail("joao@teste.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Senha123")).thenReturn("$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789");

        User saved = User.createUser("João", "joao@teste.com", "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789", null, null, true);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = service.execute(command);

        assertNotNull(result);
        verify(userRepository).findByEmail("joao@teste.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        RegisterUserCommand command = new RegisterUserCommand("João", "joao@teste.com", "Senha123");
        User existente = User.createUser("João", "joao@teste.com", "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789", null, null, true);
        when(userRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(existente));

        assertThrows(UserAlreadyExistsException.class, () -> service.execute(command));
        verify(userRepository).findByEmail("joao@teste.com");
    }
}
