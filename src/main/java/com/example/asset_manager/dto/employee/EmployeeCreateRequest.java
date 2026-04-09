package com.example.asset_manager.dto.employee;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmployeeCreateRequest {

    @NotBlank(message = "Employee code is required.")
    @Size(max = 255, message = "Employee code must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String employeeId; // your DB column name is employee_id (string code)

    @NotBlank(message = "Full name is required.")
    @Size(max = 255, message = "Full name must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String fullName;

    @NotBlank(message = "Department is required.")
    @Size(max = 255, message = "Department must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String department;

    @Email(message = "Email format is invalid.")
    @NotBlank(message = "Email is required.")
    @Pattern(regexp = "^[^\\s@]+@jptomato\\.com$", message = "Email must end with @jptomato.com.")
    @Size(max = 255, message = "Email must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String email;

    @NotBlank(message = "Role is required.")
    @Pattern(regexp = "^(SYSTEM_ADMIN|ASSET_ADMIN|EMPLOYEE)$", message = "Role must be SYSTEM_ADMIN, ASSET_ADMIN, or EMPLOYEE.")
    @NoJapaneseCharacters
    private String role;

    @NotBlank(message = "Password is required.")
    @Size(max = 255, message = "Password must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String tempPassword;

    @NotNull(message = "Active status is required.")
    private Boolean isActive;

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTempPassword() { return tempPassword; }
    public void setTempPassword(String tempPassword) { this.tempPassword = tempPassword; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
