package com.example.asset_manager.dto.assignment;

import java.time.LocalDateTime;

public class AssignmentHistoryResponse {

    private Long id;

    private Long assetId;
    private String assetTag;
    private String assetModel;

    private Long borrowerEmployeeId;
    private String borrowerEmployeeCode;
    private String borrowerEmployeeName;

    private Long lenderEmployeeId;
    private String lenderEmployeeCode;
    private String lenderEmployeeName;

    private LocalDateTime assignedAt;
    private LocalDateTime returnedAt;

    private Integer daysBorrowed;

    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }

    public String getAssetModel() { return assetModel; }
    public void setAssetModel(String assetModel) { this.assetModel = assetModel; }

    public Long getBorrowerEmployeeId() { return borrowerEmployeeId; }
    public void setBorrowerEmployeeId(Long borrowerEmployeeId) { this.borrowerEmployeeId = borrowerEmployeeId; }

    public String getBorrowerEmployeeCode() { return borrowerEmployeeCode; }
    public void setBorrowerEmployeeCode(String borrowerEmployeeCode) { this.borrowerEmployeeCode = borrowerEmployeeCode; }

    public String getBorrowerEmployeeName() { return borrowerEmployeeName; }
    public void setBorrowerEmployeeName(String borrowerEmployeeName) { this.borrowerEmployeeName = borrowerEmployeeName; }

    public Long getLenderEmployeeId() { return lenderEmployeeId; }
    public void setLenderEmployeeId(Long lenderEmployeeId) { this.lenderEmployeeId = lenderEmployeeId; }

    public String getLenderEmployeeCode() { return lenderEmployeeCode; }
    public void setLenderEmployeeCode(String lenderEmployeeCode) { this.lenderEmployeeCode = lenderEmployeeCode; }

    public String getLenderEmployeeName() { return lenderEmployeeName; }
    public void setLenderEmployeeName(String lenderEmployeeName) { this.lenderEmployeeName = lenderEmployeeName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getReturnedAt() { return returnedAt; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }

    public Integer getDaysBorrowed() { return daysBorrowed; }
    public void setDaysBorrowed(Integer daysBorrowed) { this.daysBorrowed = daysBorrowed; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}