package com.example.asset_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.asset_manager.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
}