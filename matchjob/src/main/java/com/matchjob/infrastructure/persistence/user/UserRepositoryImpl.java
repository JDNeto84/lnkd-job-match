package com.matchjob.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserJpaEntity::toDomain);
    }
}
