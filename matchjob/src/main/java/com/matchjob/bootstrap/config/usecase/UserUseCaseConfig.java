package com.matchjob.bootstrap.config.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import com.matchjob.core.usecases.user.AuthenticateUserService;
import com.matchjob.core.usecases.user.GetCurrentUserService;
import com.matchjob.core.usecases.user.RegisterUserService;
import com.matchjob.core.usecases.user.UpdateUserPreferencesService;
import com.matchjob.core.usecases.user.ChangePasswordService;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.ports.incoming.user.AuthenticateUserUseCase;
import com.matchjob.core.ports.incoming.user.GetCurrentUserUseCase;
import com.matchjob.core.ports.incoming.user.RegisterUserUseCase;
import com.matchjob.core.ports.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.core.ports.incoming.user.ChangePasswordUseCase;
import com.matchjob.core.ports.outgoing.PasswordEncoder;
import com.matchjob.core.ports.outgoing.TokenService;
import com.matchjob.infrastructure.security.JwtTokenServiceImpl;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   PasswordEncoder passwordEncoder) {
        return new RegisterUserService(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(AuthenticationManager authenticationManager,
                                                           TokenService tokenService,
                                                           JwtTokenServiceImpl jwtService) {
        return new AuthenticateUserService(authenticationManager, tokenService, jwtService);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUserService(userRepository);
    }

    @Bean
    public UpdateUserPreferencesUseCase updateUserPreferencesUseCase(UserRepository userRepository) {
        return new UpdateUserPreferencesService(userRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository,
                                                       PasswordEncoder passwordEncoder) {
        return new ChangePasswordService(userRepository, passwordEncoder);
    }
}
