package com.matchjob.infrastructure.persistence.user;

import java.time.LocalDateTime;
import java.util.UUID;

import com.matchjob.domain.user.UserPlan;
import com.matchjob.domain.user.UserRole;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "location")
    private String location;

    @Column(name = "remote", nullable = false)
    private boolean remote;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private UserPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UserEntity() {
    }
    

    public UUID getId() {
        return id;
    }
    
    void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    
    void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }
    
    void setActive(boolean active) {
        this.active = active;
    }

    public String getKeyword() {
        return keyword;
    }
    
    void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }
    
    void setLocation(String location) {
        this.location = location;
    }

    public boolean isRemote() {
        return remote;
    }
    
    void setRemote(boolean remote) {
        this.remote = remote;
    }

    public UserPlan getPlan() {
        return plan;
    }
    
    void setPlan(UserPlan plan) {
        this.plan = plan;
    }

    public UserRole getRole() {
        return role;
    }
    
    void setRole(UserRole role) {
        this.role = role;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }
    
    void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
