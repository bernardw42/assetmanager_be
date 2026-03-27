package com.example.asset_manager.dto.auth;

public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    private Long employeeId;
    private String email;
    private String fullName;
    private String role;

    public LoginResponse(String accessToken, Long employeeId, String email, String fullName, String role) {
        this.accessToken = accessToken;
        this.employeeId = employeeId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public Long getEmployeeId() { return employeeId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}
