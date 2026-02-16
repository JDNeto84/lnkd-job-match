package com.matchjob.application.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchjob.application.rest.dto.auth.LoginRequest;
import com.matchjob.application.rest.dto.auth.LoginResponse;
import com.matchjob.application.rest.dto.auth.MeResponse;
import com.matchjob.application.rest.dto.auth.RegisterRequest;
import com.matchjob.core.domain.entity.User;
import com.matchjob.core.ports.incoming.user.AuthenticateUserUseCase;
import com.matchjob.core.ports.incoming.user.GetCurrentUserUseCase;
import com.matchjob.core.ports.incoming.user.RegisterUserUseCase;
import com.matchjob.core.usecases.user.dto.AuthenticateUserCommand;
import com.matchjob.core.usecases.user.dto.GetCurrentUserQuery;
import com.matchjob.core.usecases.user.dto.RegisterUserCommand;

 

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUser;
    private final RegisterUserUseCase registerUser;
    private final GetCurrentUserUseCase getCurrentUser;

    public AuthController(AuthenticateUserUseCase authenticateUser,
                          RegisterUserUseCase registerUser,
                          GetCurrentUserUseCase getCurrentUser) {
        this.authenticateUser = authenticateUser;
        this.registerUser = registerUser;
        this.getCurrentUser = getCurrentUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUser.execute(new AuthenticateUserCommand(request.email(), request.password()));
        return ResponseEntity.ok(new LoginResponse(result.token(), result.tokenType(), result.expiresInSeconds()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User saved = registerUser.execute(new RegisterUserCommand(request.name(), request.email(), request.password()));

        MeResponse response = new MeResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                "ROLE_" + saved.getRole().name(),
                saved.getPlan().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getUsername();

        var userOpt = getCurrentUser.execute(new GetCurrentUserQuery(email));

        if (userOpt.isEmpty()) {
            String role = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
            MeResponse response = new MeResponse(null, null, email, role, null);
            return ResponseEntity.ok(response);
        }

        User user = userOpt.get();
        String role = "ROLE_" + user.getRole().name();
        MeResponse response = new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                role,
                user.getPlan().name()
        );
        return ResponseEntity.ok(response);
    }
}
