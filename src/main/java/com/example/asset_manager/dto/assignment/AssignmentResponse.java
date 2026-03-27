package com.example.asset_manager.dto.assignment;

import java.time.LocalDateTime;

public class AssignmentResponse {

    private Long id;
    private Long assetId;
    private Long employeeId;
    private Long requestId;
    private LocalDateTime assignedAt;
    private LocalDateTime returnedAt;
    private String note;

    // optional “joined” info for FE convenience
    private String assetTag;
    private String assetModel;
    private String employeeCode;
    private String employeeName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }
    public String getAssetModel() { return assetModel; }
    public void setAssetModel(String assetModel) { this.assetModel = assetModel; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
}
