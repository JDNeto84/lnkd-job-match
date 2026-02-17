package com.matchjob.infrastructure.config.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import com.matchjob.application.port.incoming.user.AuthenticateUserUseCase;
import com.matchjob.application.port.incoming.user.ChangePasswordUseCase;
import com.matchjob.application.port.incoming.user.GetCurrentUserUseCase;
import com.matchjob.application.port.incoming.user.RegisterUserUseCase;
import com.matchjob.application.port.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.application.port.outgoing.TokenService;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.AuthenticateUserUseCaseImpl;
import com.matchjob.application.user.GetCurrentUserUseCaseImpl;
import com.matchjob.application.user.RegisterUserUseCaseImpl;
import com.matchjob.application.user.UpdateUserPreferencesUseCaseImpl;
import com.matchjob.application.user.ChangePasswordUseCaseImpl;
import com.matchjob.infrastructure.security.JwtTokenServiceImpl;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCaseImpl(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(AuthenticationManager authenticationManager,
                                                           TokenService tokenService,
                                                           JwtTokenServiceImpl jwtService) {
        return new AuthenticateUserUseCaseImpl(authenticationManager, tokenService, jwtService);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUserUseCaseImpl(userRepository);
    }

    @Bean
    public UpdateUserPreferencesUseCase updateUserPreferencesUseCase(UserRepository userRepository) {
        return new UpdateUserPreferencesUseCaseImpl(userRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository,
                                                       PasswordEncoder passwordEncoder) {
        return new ChangePasswordUseCaseImpl(userRepository, passwordEncoder);
    }
}
