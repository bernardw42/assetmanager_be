package com.example.asset_manager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:1 link to employees.id
    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false, length = 30)
    private String role; // EMPLOYEE / ASSET_ADMIN / SYSTEM_ADMIN

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_password_changed_at")
    private LocalDateTime lastPasswordChangedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public UserAccount() {}

    public Long getId() { return id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getLastPasswordChangedAt() { return lastPasswordChangedAt; }
    public void setLastPasswordChangedAt(LocalDateTime lastPasswordChangedAt) { this.lastPasswordChangedAt = lastPasswordChangedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}