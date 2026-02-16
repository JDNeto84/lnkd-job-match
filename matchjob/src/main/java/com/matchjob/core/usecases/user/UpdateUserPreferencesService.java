package com.matchjob.core.usecases.user;

import java.util.UUID;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.exception.UserNotFoundException;
import com.matchjob.core.domain.repository.UserRepository;
import com.matchjob.core.ports.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.core.usecases.user.dto.UpdateUserPreferencesCommand;

public class UpdateUserPreferencesService implements UpdateUserPreferencesUseCase {

    private final UserRepository userRepository;

    public UpdateUserPreferencesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(UpdateUserPreferencesCommand command) {
        UUID userId = command.userId();
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        User current = userOpt.get();
        String keyword = command.keyword();
        String location = command.location();
        Boolean remotePreferred = command.remotePreferred();

        User updated = new UserBuilderFrom(current)
                .keyword(keyword)
                .location(location)
                .remotePreferred(remotePreferred)
                .build();

        return userRepository.save(updated);
    }

    private static class UserBuilderFrom {
        private final User base;
        private String keyword;
        private String location;
        private Boolean remotePreferred;

        UserBuilderFrom(User base) {
            this.base = base;
        }

        UserBuilderFrom keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        UserBuilderFrom location(String location) {
            this.location = location;
            return this;
        }

        UserBuilderFrom remotePreferred(Boolean remotePreferred) {
            this.remotePreferred = remotePreferred;
            return this;
        }

        User build() {
            String newKeyword = keyword != null ? keyword : base.getKeyword();
            String newLocation = location != null ? location : base.getLocation();
            boolean newRemote = remotePreferred != null ? remotePreferred : base.isRemote();

            return User.reconstructUser(
                    base.getId(),
                    base.getName(),
                    base.getEmail(),
                    base.getPassword(),
                    base.getPlan(),
                    base.getRole(),
                    base.isActive(),
                    newKeyword != null ? newKeyword : null,
                    newLocation != null ? newLocation : null,
                    newRemote,
                    base.getCreatedAt(),
                    java.time.LocalDateTime.now()
            );
        }
    }
}
