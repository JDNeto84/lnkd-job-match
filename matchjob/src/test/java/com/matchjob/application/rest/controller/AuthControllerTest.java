package com.matchjob.application.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.matchjob.application.rest.dto.auth.LoginRequest;
import com.matchjob.application.rest.dto.auth.LoginResponse;
import com.matchjob.application.rest.dto.auth.MeResponse;
import com.matchjob.application.rest.dto.auth.RegisterRequest;
import com.matchjob.core.domain.entity.UserPlan;
import com.matchjob.core.domain.entity.UserRole;
import com.matchjob.core.ports.incoming.user.AuthenticateUserUseCase;
import com.matchjob.core.ports.incoming.user.GetCurrentUserUseCase;
import com.matchjob.core.ports.incoming.user.RegisterUserUseCase;
import com.matchjob.core.usecases.user.dto.AuthenticateUserCommand;
import com.matchjob.core.usecases.user.dto.AuthenticateUserResult;
import com.matchjob.core.usecases.user.dto.GetCurrentUserQuery;
import com.matchjob.core.usecases.user.dto.RegisterUserCommand;

class AuthControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @InjectMocks
    private AuthController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveFazerLoginERetornarToken() {
        LoginRequest request = new LoginRequest("user@teste.com", "Senha123");
        AuthenticateUserResult result = new AuthenticateUserResult("token-jwt", "Bearer", 3600L);

        when(authenticateUserUseCase.execute(any(AuthenticateUserCommand.class))).thenReturn(result);

        ResponseEntity<?> responseEntity = controller.login(request);
        assertEquals(200, responseEntity.getStatusCode().value());
        Object body = responseEntity.getBody();
        assertNotNull(body);
        LoginResponse response = (LoginResponse) body;
        assertEquals("token-jwt", response.token());
        assertEquals("Bearer", response.type());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void deveRegistrarUsuario() {
        RegisterRequest request = new RegisterRequest("João", "user@teste.com", "Senha123");

        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(registerUserUseCase.execute(any(RegisterUserCommand.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = controller.register(request);
        assertEquals(200, responseEntity.getStatusCode().value());
        MeResponse body = (MeResponse) responseEntity.getBody();
        assertNotNull(body);
        assertEquals("user@teste.com", body.email());
    }

    @Test
    void deveRetornar401QuandoPrincipalNuloEmMe() {
        ResponseEntity<?> responseEntity = controller.me(null);
        assertEquals(401, responseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());
    }

    @Test
    void deveRetornarDadosDoUsuarioEmMeQuandoUsuarioExiste() {
        com.matchjob.core.domain.entity.User user = com.matchjob.core.domain.entity.User.createUser(
                "João",
                "user@teste.com",
                "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWX12345678901234567890123456789",
                UserPlan.FREE,
                UserRole.USER,
                true
        );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserQuery.class))).thenReturn(Optional.of(user));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User principal = new User("user@teste.com", "senha", authorities);

        ResponseEntity<?> responseEntity = controller.me(principal);
        assertEquals(200, responseEntity.getStatusCode().value());
        MeResponse body = (MeResponse) responseEntity.getBody();
        assertNotNull(body);
        assertEquals("user@teste.com", body.email());
    }
}
