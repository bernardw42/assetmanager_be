package com.example.asset_manager.dto.auth;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ForgotPasswordRequest {

    @Email(message = "Email format is invalid.")
    @NotBlank(message = "Email is required.")
    @Pattern(regexp = "^[^\\s@]+@jptomato\\.com$", message = "Email must end with @jptomato.com.")
    @Size(max = 255, message = "Email must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
