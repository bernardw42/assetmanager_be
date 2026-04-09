package com.example.asset_manager.dto.request;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AssetRequestCreateRequest {

    @NotBlank(message = "Request type is required.")
    @Pattern(regexp = "^(LOAN|EXCHANGE)$", message = "Request type must be LOAN or EXCHANGE.")
    @NoJapaneseCharacters
    private String requestType; // LOAN / EXCHANGE

    @Size(max = 255, message = "Minimum CPU must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String minCpuAmt;
    @Positive(message = "Minimum RAM must be greater than 0.")
    private Integer minRamAmt;
    @Positive(message = "Minimum storage must be greater than 0.")
    private Integer minStorageAmt;
    @Size(max = 1000, message = "Reason must be 1000 characters or fewer.")
    @NoJapaneseCharacters
    private String reason;

    @NotNull(message = "Employee is required.")
    private Long employeeId; // v0: employee chooses; later derive from login

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getMinCpuAmt() { return minCpuAmt; }
    public void setMinCpuAmt(String minCpuAmt) { this.minCpuAmt = minCpuAmt; }
    public Integer getMinRamAmt() { return minRamAmt; }
    public void setMinRamAmt(Integer minRamAmt) { this.minRamAmt = minRamAmt; }
    public Integer getMinStorageAmt() { return minStorageAmt; }
    public void setMinStorageAmt(Integer minStorageAmt) { this.minStorageAmt = minStorageAmt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}
