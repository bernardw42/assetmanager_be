package com.example.asset_manager.dto.request;

import java.time.LocalDateTime;

public class AssetRequestResponse {

    private Long id;
    private Long employeeId;
    private String requestType;
    private String minCpuAmt;
    private Integer minRamAmt;
    private Integer minStorageAmt;
    private String reason;

    private String unitStatus;

    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private String reviewComment;

    private Long requestAssetId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
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
    public String getUnitStatus() { return unitStatus; }
    public void setUnitStatus(String unitStatus) { this.unitStatus = unitStatus; }
    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    public Long getRequestAssetId() { return requestAssetId; }
    public void setRequestAssetId(Long requestAssetId) { this.requestAssetId = requestAssetId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
