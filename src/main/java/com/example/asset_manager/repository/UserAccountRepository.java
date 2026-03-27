package com.example.asset_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.asset_manager.entity.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmployeeId(Long employeeId);
    boolean existsByEmployeeId(Long employeeId);
}
