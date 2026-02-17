package com.matchjob.infrastructure.persistence.user;

import org.springframework.stereotype.Component;

import com.matchjob.domain.user.User;
import com.matchjob.domain.valueobject.Name;
import com.matchjob.domain.valueobject.Email;
import com.matchjob.domain.valueobject.Password;
import com.matchjob.domain.valueobject.Keyword;
import com.matchjob.domain.valueobject.Location;

@Component
public class UserMapper {

    public UserEntity toJpa(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPasswordVo().value());
        entity.setActive(user.isActive());
        entity.setKeyword(user.getKeyword());
        entity.setLocation(user.getLocation());
        entity.setRemote(user.isRemote());
        entity.setPlan(user.getPlan());
        entity.setRole(user.getRole());
        entity.setTelegramChatId(user.getTelegramChatId());
        return entity;
    }

    public void applyToEntity(User user, UserEntity entity) {
        entity.setName(user.getName());
        entity.setPassword(user.getPasswordVo().value());
        entity.setActive(user.isActive());
        entity.setKeyword(user.getKeyword());
        entity.setLocation(user.getLocation());
        entity.setRemote(user.isRemote());
        entity.setPlan(user.getPlan());
        entity.setRole(user.getRole());
        entity.setTelegramChatId(user.getTelegramChatId());
    }

    public User toDomain(UserEntity entity) {
        return User.reconstructUser(
                entity.getId(),
                new Name(entity.getName()),
                new Email(entity.getEmail()),
                Password.fromHashed(entity.getPassword()),
                entity.getPlan(),
                entity.getRole(),
                entity.isActive(),
                entity.getKeyword() != null ? new Keyword(entity.getKeyword()) : null,
                entity.getLocation() != null ? new Location(entity.getLocation()) : null,
                entity.isRemote(),
                entity.getTelegramChatId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
