package com.example.asset_manager.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.asset_manager.dto.auth.ForgotPasswordRequest;
import com.example.asset_manager.dto.auth.ResetPasswordRequest;

import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.entity.PasswordResetToken;
import com.example.asset_manager.entity.UserAccount;

import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.EmployeeRepository;
import com.example.asset_manager.repository.PasswordResetTokenRepository;
import com.example.asset_manager.repository.UserAccountRepository;

@Service
public class PasswordResetService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.reset.base-url}")
    private String resetBaseUrl;

    @Value("${app.reset.token-expiry-minutes:60}")
    private long expiryMinutes;

    @Value("${app.mail.from:no-reply@jptomato.com}")
    private String mailFrom;

    public PasswordResetService(
            EmployeeRepository employeeRepository,
            UserAccountRepository userAccountRepository,
            PasswordResetTokenRepository tokenRepository,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Security best practice: ALWAYS return success even if email not found.
     * Prevents attackers from checking which emails exist.
     */
    public void requestReset(ForgotPasswordRequest req) {
        String email = normalizeEmail(req.getEmail());

        Employee emp = employeeRepository.findByEmail(email).orElse(null);
        if (emp == null) {
            return;
        }

        UserAccount ua = userAccountRepository.findByEmployeeId(emp.getId()).orElse(null);
        if (ua == null || Boolean.FALSE.equals(ua.getIsActive())) {
            return;
        }

        String rawToken = generateRawToken();
        String tokenHash = sha256Base64(rawToken);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setEmployeeId(emp.getId());
        prt.setTokenHash(tokenHash);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        prt.setUsedAt(null);

        tokenRepository.save(prt);

        String link = buildResetLink(rawToken);
        sendResetEmail(emp.getEmail(), link);
    }

    public void resetPassword(ResetPasswordRequest req) {
        String rawToken = requireText(req.getToken(), "Reset token");
        String newPassword = validatePassword(req.getNewPassword(), "New password");
        String tokenHash = sha256Base64(rawToken);

        PasswordResetToken prt = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new NotFoundException("Invalid reset token"));

        if (prt.getUsedAt() != null) {
            throw new IllegalStateException("Reset token already used");
        }

        if (LocalDateTime.now().isAfter(prt.getExpiresAt())) {
            throw new IllegalStateException("Reset token expired");
        }

        UserAccount ua = userAccountRepository.findByEmployeeId(prt.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("User account not found"));

        if (passwordEncoder.matches(newPassword, ua.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }

        ua.setPasswordHash(passwordEncoder.encode(newPassword));
        ua.setLastPasswordChangedAt(LocalDateTime.now());
        userAccountRepository.save(ua);

        prt.setUsedAt(LocalDateTime.now());
        tokenRepository.save(prt);
    }

    private String generateRawToken() {
        // long random string
        return UUID.randomUUID().toString().replace("-", "")
             + UUID.randomUUID().toString().replace("-", "");
    }

    private String buildResetLink(String rawToken) {
        if (resetBaseUrl.contains("?")) {
            return resetBaseUrl + "&token=" + rawToken;
        }
        return resetBaseUrl + "?token=" + rawToken;
    }

    private void sendResetEmail(String to, String link) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailFrom);
        msg.setTo(to);
        msg.setSubject("[Asset Manager] Password Reset");
        msg.setText(
                "You requested a password reset.\n\n" +
                "This link is valid for " + expiryMinutes + " minutes:\n" +
                link + "\n\n" +
                "If you did not request this, ignore this email."
        );
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            throw new IllegalStateException("We could not send the reset email right now. Please contact the system administrator.");
        }
    }

    private String sha256Base64(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("Hashing failed", e);
        }
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
