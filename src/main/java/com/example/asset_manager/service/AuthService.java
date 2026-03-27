package com.example.asset_manager.service;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.asset_manager.dto.auth.ChangePasswordRequest;
import com.example.asset_manager.dto.auth.LoginRequest;
import com.example.asset_manager.dto.auth.LoginResponse;
import com.example.asset_manager.entity.Employee;

import com.example.asset_manager.entity.UserAccount;

import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.repository.UserAccountRepository;
import com.example.asset_manager.security.CustomUserDetails;
import com.example.asset_manager.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            EmployeeRepository employeeRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest req) {
        String email = normalizeEmail(req.getEmail());
        String password = requireText(req.getPassword(), "Password");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        Employee employee = employeeRepository.findById(user.getEmployeeId()).orElse(null);
        String fullName = employee != null ? employee.getFullName() : user.getEmail();

        return new LoginResponse(token, user.getEmployeeId(), user.getEmail(), fullName, user.getRole());
    }

    public void changePassword(CustomUserDetails user, ChangePasswordRequest req) {
        UserAccount ua = userAccountRepository.findByEmployeeId(user.getEmployeeId())
                .orElseThrow(() -> new IllegalStateException("User account not found"));

        String oldPassword = requireText(req.getOldPassword(), "Current password");
        String newPassword = validatePassword(req.getNewPassword(), "New password");

        if (!passwordEncoder.matches(oldPassword, ua.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, ua.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }

        ua.setPasswordHash(passwordEncoder.encode(newPassword));
        ua.setLastPasswordChangedAt(LocalDateTime.now());
        userAccountRepository.save(ua);
    }

    private String normalizeEmail(String email) {
        String normalized = requireText(email, "Email").toLowerCase(Locale.ROOT);
        if (!normalized.endsWith("@jptomato.com")) {
            throw new IllegalArgumentException("Email must end with @jptomato.com.");
        }
        return normalized;
    }

    private String validatePassword(String password, String label) {
        String normalized = requireText(password, label);
        if (normalized.length() < 8) {
            throw new IllegalArgumentException(label + " must be at least 8 characters.");
        }
        if (normalized.length() > 255) {
            throw new IllegalArgumentException(label + " must be 255 characters or fewer.");
        }
        return normalized;
    }

    private String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " is required.");
        }
        return value.trim();
    }
}
