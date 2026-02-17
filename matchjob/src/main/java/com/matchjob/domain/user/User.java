package com.matchjob.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import com.matchjob.application.port.outgoing.PasswordEncoder;
import com.matchjob.domain.valueobject.Email;
import com.matchjob.domain.valueobject.Keyword;
import com.matchjob.domain.valueobject.Location;
import com.matchjob.domain.valueobject.Name;
import com.matchjob.domain.valueobject.Password;

public class User {
    private final UUID id;
    private final Name name;
    private final Email email;
    private final Password password;
    private final boolean isActive;
    private final Keyword keyword;
    private final Location location;
    private final boolean isRemote;
    private final UserPlan plan;
    private final UserRole role;
    private final Long telegramChatId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(UUID id,
                 Name name,
                 Email email,
                 Password password,
                 boolean isActive,
                 Keyword keyword,
                 Location location,
                 boolean isRemote,
                 UserPlan plan,
                 UserRole role,
                 Long telegramChatId,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.keyword = keyword;
        this.location = location;
        this.isRemote = isRemote;
        this.plan = plan;
        this.role = role;
        this.telegramChatId = telegramChatId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNewUser(String name,
                                     String email,
                                     String rawPassword,
                                     UserPlan plan,
                                     UserRole role,
                                     PasswordEncoder passwordEncoder) {
        LocalDateTime now = LocalDateTime.now();
        return new User(
                UUID.randomUUID(),
                new Name(name),
                new Email(email),
                Password.create(rawPassword, passwordEncoder),
                true,
                null,
                null,
                false,
                plan != null ? plan : UserPlan.FREE,
                role != null ? role : UserRole.USER,
                null,
                now,
                now
        );
    }

    public static User reconstructUser(UUID id,
                                       Name name,
                                       Email email,
                                       Password password,
                                       UserPlan plan,
                                       UserRole role,
                                       boolean active,
                                       Keyword keyword,
                                       Location location,
                                       boolean isRemote,
                                       Long telegramChatId,
                                       LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {
        return new User(
                id,
                name,
                email,
                password,
                active,
                keyword,
                location,
                isRemote,
                plan != null ? plan : UserPlan.FREE,
                role != null ? role : UserRole.USER,
                telegramChatId,
                createdAt,
                updatedAt
        );
    }

    public User withPassword(Password newPassword) {
        return reconstructUser(
                id,
                name,
                email,
                newPassword,
                plan,
                role,
                isActive,
                keyword,
                location,
                isRemote,
                getTelegramChatId(),
                createdAt,
                LocalDateTime.now()
        );
    }

    public User withLocation(Location location) {
        Location newLocation = location;
        return reconstructUser(
                id,
                name,
                email,
                password,
                plan,
                role,
                isActive,
                keyword,
                newLocation,
                isRemote,
                getTelegramChatId(),
                createdAt,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name != null ? name.value() : null;
    }

    public Name getNameVo() {
        return name;
    }

    public String getEmail() {
        return email != null ? email.value() : null;
    }

    public Password getPasswordVo() {
        return password;
    }

    public Email getEmailVo() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getKeyword() {
        return keyword != null ? keyword.value() : null;
    }

    public Keyword getKeywordVo() {
        return keyword;
    }

    public String getLocation() {
        return location != null ? location.value() : null;
    }

    public Location getLocationVo() {
        return location;
    }

    public boolean isRemote() {
        return isRemote;
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

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return id.equals(other.id);
    }
}
