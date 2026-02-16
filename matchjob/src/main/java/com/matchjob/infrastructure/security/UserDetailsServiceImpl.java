package com.matchjob.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matchjob.infrastructure.persistence.user.UserJpaRepository;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userRepository;

    public UserDetailsServiceImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username != null ? username.trim().toLowerCase() : null;
        var userEntity = userRepository.findByEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!userEntity.isActive()) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())
        );

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }
}
