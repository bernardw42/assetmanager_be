package com.example.asset_manager.dto.auth;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {

    @Email(message = "Email format is invalid.")
    @NotBlank(message = "Email is required.")
    @Pattern(regexp = "^[^\\s@]+@jptomato\\.com$", message = "Email must end with @jptomato.com.")
    @NoJapaneseCharacters
    private String email;

    @NotBlank(message = "Password is required.")
    @NoJapaneseCharacters
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
