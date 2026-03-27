package com.example.asset_manager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assets_request")
public class AssetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // requester
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "request_type", nullable = false, length = 50)
    private String requestType; // LOAN / EXCHANGE

    @Column(name = "min_cpu_amt")
    private String minCpuAmt;

    @Column(name = "min_ram_amt")
    private Integer minRamAmt;

    @Column(name = "min_storage_amt")
    private Integer minStorageAmt;

    @Column(name = "reason")
    private String reason;

    // request status
    @Column(name = "unit_status", nullable = false, length = 50)
    private String unitStatus; // PENDING / APPROVED / REJECTED / FULFILLED / CANCELLED

    // reviewer
    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "review_comment")
    private String reviewComment;

    // fulfilled with which asset
    @Column(name = "request_asset_id")
    private Long requestAssetId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public AssetRequest() {}

    public Long getId() { return id; }
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
