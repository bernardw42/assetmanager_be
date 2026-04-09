package com.example.asset_manager.dto.auth;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    @Size(max = 255, message = "Current password must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String oldPassword;

    @NotBlank(message = "New password is required.")
    @Size(min = 8, max = 255, message = "New password must be between 8 and 255 characters.")
    @NoJapaneseCharacters
    private String newPassword;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
