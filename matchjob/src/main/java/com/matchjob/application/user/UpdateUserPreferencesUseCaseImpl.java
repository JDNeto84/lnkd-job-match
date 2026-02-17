package com.matchjob.application.user;

import java.util.UUID;

import com.matchjob.domain.exception.UserNotFoundException;
import com.matchjob.domain.user.User;
import com.matchjob.domain.valueobject.Keyword;
import com.matchjob.domain.valueobject.Location;
import com.matchjob.application.port.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.application.port.outgoing.UserRepository;
import com.matchjob.application.user.dto.UpdateUserPreferencesCommand;

public class UpdateUserPreferencesUseCaseImpl implements UpdateUserPreferencesUseCase {

    private final UserRepository userRepository;

    public UpdateUserPreferencesUseCaseImpl(UserRepository userRepository) {
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

            Keyword keywordVo = newKeyword != null ? new Keyword(newKeyword) : base.getKeywordVo();
            Location locationVo = newLocation != null ? new Location(newLocation) : base.getLocationVo();

            return User.reconstructUser(
                    base.getId(),
                    base.getNameVo(),
                    base.getEmailVo(),
                    base.getPasswordVo(),
                    base.getPlan(),
                    base.getRole(),
                    base.isActive(),
                    keywordVo,
                    locationVo,
                    newRemote,
                    base.getTelegramChatId(),
                    base.getCreatedAt(),
                    java.time.LocalDateTime.now()
            );
        }
    }
}
