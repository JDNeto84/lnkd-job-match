package com.matchjob.application.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.matchjob.application.port.incoming.user.AuthenticateUserUseCase;
import com.matchjob.application.port.outgoing.TokenService;
import com.matchjob.application.user.dto.AuthenticateUserCommand;
import com.matchjob.application.user.dto.AuthenticateUserResult;
import com.matchjob.infrastructure.security.JwtTokenServiceImpl;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JwtTokenServiceImpl jwtService;

    public AuthenticateUserUseCaseImpl(AuthenticationManager authenticationManager,
                                       TokenService tokenService,
                                       JwtTokenServiceImpl jwtService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticateUserResult execute(AuthenticateUserCommand command) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(command.email(), command.password())
            );
        } catch (AuthenticationException ex) {
            throw ex;
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = tokenService.generateToken(principal.getUsername(), roles);
        long expiresInSeconds = jwtService.getExpirationMinutes() * 60L;

        return new AuthenticateUserResult(token, "Bearer", expiresInSeconds);
    }
}
