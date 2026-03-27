package com.example.asset_manager.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Long employeeId;
    private final String email;
    private final String passwordHash;
    private final String role;
    private final boolean active;

    public CustomUserDetails(Long employeeId, String email, String passwordHash, String role, boolean active) {
        this.employeeId = employeeId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
    }

    public Long getEmployeeId() { return employeeId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring expects ROLE_ prefix when using hasRole(...)
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() { return passwordHash; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }
}