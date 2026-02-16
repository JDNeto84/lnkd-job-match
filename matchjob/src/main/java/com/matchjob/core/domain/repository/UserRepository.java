package com.matchjob.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.matchjob.core.domain.entity.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}
