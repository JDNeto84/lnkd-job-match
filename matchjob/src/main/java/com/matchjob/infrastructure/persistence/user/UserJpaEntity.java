package com.matchjob.infrastructure.persistence.user;

import java.time.LocalDateTime;
import java.util.UUID;

import com.matchjob.core.domain.entity.User;
import com.matchjob.core.domain.entity.UserPlan;
import com.matchjob.core.domain.entity.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    private String keyword;
    private String location;
    private boolean remote;

    @Enumerated(EnumType.STRING)
    private UserPlan plan;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Long telegramChatId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected UserJpaEntity() {
    }

    public static UserJpaEntity fromDomain(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.id = user.getId();
        entity.name = user.getName();
        entity.email = user.getEmail();
        entity.password = user.getPassword();
        entity.active = user.isActive();
        entity.keyword = user.getKeyword();
        entity.location = user.getLocation();
        entity.remote = user.isRemote();
        entity.plan = user.getPlan();
        entity.role = user.getRole();
        entity.telegramChatId = user.getTelegramChatId();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        return entity;
    }

    public User toDomain() {
        return User.reconstructUser(
                id,
                name,
                email,
                password,
                plan,
                role,
                active,
                keyword,
                location,
                remote,
                createdAt,
                updatedAt
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLocation() {
        return location;
    }

    public boolean isRemote() {
        return remote;
    }

    public UserPlan getPlan() {
        return plan;
    }

    public UserRole getRole() {
        return role;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
