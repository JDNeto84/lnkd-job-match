package com.matchjob.application.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.matchjob.application.rest.dto.auth.MeResponse;
import com.matchjob.application.rest.dto.user.ChangePasswordRequest;
import com.matchjob.application.rest.dto.user.UpdatePreferencesRequest;
import com.matchjob.core.domain.entity.UserPlan;
import com.matchjob.core.domain.entity.UserRole;
import com.matchjob.core.ports.incoming.user.ChangePasswordUseCase;
import com.matchjob.core.ports.incoming.user.GetCurrentUserUseCase;
import com.matchjob.core.ports.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.core.usecases.user.dto.ChangePasswordCommand;
import com.matchjob.core.usecases.user.dto.GetCurrentUserQuery;
import com.matchjob.core.usecases.user.dto.UpdateUserPreferencesCommand;

class UserControllerTest {

    @Mock
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @Mock
    private UpdateUserPreferencesUseCase updateUserPreferencesUseCase;

    @Mock
    private ChangePasswordUseCase changePasswordUseCase;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornar401QuandoPrincipalNuloEmUpdatePreferences() {
        UpdatePreferencesRequest request = new UpdatePreferencesRequest("product manager", "São Paulo, SP", true);
        ResponseEntity<?> response = controller.updatePreferences(null, request);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void deveAtualizarPreferenciasDoUsuario() {
        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserQuery.class))).thenReturn(Optional.of(user));
        when(updateUserPreferencesUseCase.execute(any(UpdateUserPreferencesCommand.class))).thenReturn(user);

        UpdatePreferencesRequest request = new UpdatePreferencesRequest(
                "product manager",
                "São Paulo, SP",
                true
        );

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("user@teste.com", "senha", authorities);

        ResponseEntity<?> responseEntity = controller.updatePreferences(principal, request);
        assertEquals(200, responseEntity.getStatusCode().value());
        MeResponse body = (MeResponse) responseEntity.getBody();
        assertNotNull(body);
        assertEquals("user@teste.com", body.email());
    }

    @Test
    void deveDefinirBrasilQuandoLocationVazioEmUpdatePreferences() {
        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserQuery.class))).thenReturn(Optional.of(user));
        when(updateUserPreferencesUseCase.execute(any(UpdateUserPreferencesCommand.class))).thenReturn(user);

        UpdatePreferencesRequest request = new UpdatePreferencesRequest(
                "Desenvolvedor",
                "",
                true
        );

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("user@teste.com", "senha", authorities);

        ResponseEntity<?> responseEntity = controller.updatePreferences(principal, request);
        assertEquals(200, responseEntity.getStatusCode().value());

        ArgumentCaptor<UpdateUserPreferencesCommand> captor = ArgumentCaptor.forClass(UpdateUserPreferencesCommand.class);
        verify(updateUserPreferencesUseCase).execute(captor.capture());
        UpdateUserPreferencesCommand passed = captor.getValue();
        assertEquals("Brasil", passed.location());
    }

    @Test
    void devePreservarLocationQuandoDefinidoEmUpdatePreferences() {
        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserQuery.class))).thenReturn(Optional.of(user));
        when(updateUserPreferencesUseCase.execute(any(UpdateUserPreferencesCommand.class))).thenReturn(user);

        UpdatePreferencesRequest request = new UpdatePreferencesRequest(
                "Desenvolvedor",
                "São Paulo",
                true
        );

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("user@teste.com", "senha", authorities);

        ResponseEntity<?> responseEntity = controller.updatePreferences(principal, request);
        assertEquals(200, responseEntity.getStatusCode().value());

        ArgumentCaptor<UpdateUserPreferencesCommand> captor = ArgumentCaptor.forClass(UpdateUserPreferencesCommand.class);
        verify(updateUserPreferencesUseCase).execute(captor.capture());
        UpdateUserPreferencesCommand passed = captor.getValue();
        assertEquals("São Paulo", passed.location());
    }

    @Test
    void deveRetornar401QuandoPrincipalNuloEmChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("SenhaAtual123", "NovaSenha123");
        ResponseEntity<?> response = controller.changePassword(null, request);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void deveAlterarSenhaDoUsuario() {
        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserQuery.class))).thenReturn(Optional.of(user));
        when(changePasswordUseCase.execute(any(ChangePasswordCommand.class))).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("SenhaAtual123", "NovaSenha123");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("user@teste.com", "senha", authorities);

        ResponseEntity<?> responseEntity = controller.changePassword(principal, request);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());
    }
}
