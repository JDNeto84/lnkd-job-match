package com.matchjob.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.matchjob.core.domain.valueobject.Location;
import com.matchjob.core.domain.valueobject.Keyword;
import com.matchjob.core.domain.valueobject.Name;
import com.matchjob.core.domain.valueobject.Email;
import com.matchjob.core.domain.valueobject.Password;

public class User {
    private UUID id;
    private Name name;
    private Email email;
    private Password password;
    private boolean isActive;
    private Keyword keyword;
    private Location location;
    private boolean isRemote;
    private UserPlan plan;
    private UserRole role;
    private Long telegramChatId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User() {
    }

    public static User createUser(String name, String email, String password, UserPlan plan, UserRole role, boolean active) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.name = new Name(name);
        user.email = new Email(email);
        user.password = Password.fromHashed(password);
        user.isActive = active;
        user.keyword = null;
        user.location = null;
        user.isRemote = false;
        user.plan = plan != null ? plan : UserPlan.FREE;
        user.role = role != null ? role : UserRole.USER;
        user.telegramChatId = null;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }

    public static User reconstructUser(UUID id, String name, String email, String password, UserPlan plan, UserRole role, boolean active, String keyword, String location, boolean isRemote, LocalDateTime createdAt, LocalDateTime updatedAt) {
        User user = new User();
        user.id = id;
        user.name = new Name(name);
        user.email = new Email(email);
        user.password = Password.fromHashed(password);
        user.isActive = active;
        user.keyword = keyword != null ? new Keyword(keyword) : null;
        user.location = location != null ? new Location(location) : null;
        user.isRemote = isRemote;
        user.plan = plan != null ? plan : UserPlan.FREE;
        user.role = role != null ? role : UserRole.USER;
        user.telegramChatId = null;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        return user;
    }

    public User withPassword(String newPassword) {
        return reconstructUser(
                id,
                getName(),
                getEmail(),
                newPassword,
                plan,
                role,
                isActive,
                getKeyword(),
                getLocation(),
                isRemote,
                createdAt,
                LocalDateTime.now()
        );
    }

    public User withLocation(Location location) {
        String locationValue = location != null ? location.value() : null;
        return reconstructUser(
                id,
                getName(),
                getEmail(),
                getPassword(),
                plan,
                role,
                isActive,
                getKeyword(),
                locationValue,
                isRemote,
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

    public String getPassword() {
        return password != null ? password.value() : null;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
