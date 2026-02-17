package com.matchjob.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.domain.user.User;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserMapper mapper;

    public JpaUserRepository(SpringDataUserRepository springDataUserRepository, UserMapper mapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            var existingOpt = springDataUserRepository.findById(user.getId());
            if (existingOpt.isPresent()) {
                UserEntity existing = existingOpt.get();
                mapper.applyToEntity(user, existing);
                UserEntity saved = springDataUserRepository.save(existing);
                return mapper.toDomain(saved);
            }
        }
        UserEntity entity = mapper.toJpa(user);
        UserEntity saved = springDataUserRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id).map(mapper::toDomain);
    }
}
