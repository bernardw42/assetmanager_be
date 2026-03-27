package com.example.asset_manager.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.asset_manager.dto.auth.ChangePasswordRequest;
import com.example.asset_manager.dto.auth.ForgotPasswordRequest;
import com.example.asset_manager.dto.auth.LoginRequest;
import com.example.asset_manager.dto.auth.LoginResponse;
import com.example.asset_manager.dto.auth.ResetPasswordRequest;

import com.example.asset_manager.security.CustomUserDetails;

import com.example.asset_manager.service.AuthService;
import com.example.asset_manager.service.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    // protected (anyRequest().authenticated()) already covers this, but keeping is ok
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/change-password")
    public void changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest req) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        authService.changePassword(user, req);
    }

    // PUBLIC: /api/auth/** is permitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        passwordResetService.requestReset(req);
    }

    // PUBLIC: link-based reset, no login required
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        passwordResetService.resetPassword(req);
    }
}