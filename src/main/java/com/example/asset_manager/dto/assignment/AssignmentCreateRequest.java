package com.example.asset_manager.dto.assignment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AssignmentCreateRequest {

    @NotNull(message = "Asset is required.")
    private Long assetId;

    private Long employeeId; // borrower (employees.id)

    private Long requestId;

    @NotNull(message = "Approving admin is required.")
    private Long approvedBy; // lender/admin (employees.id)

    @Size(max = 1000, message = "Note must be 1000 characters or fewer.")
    private String note;

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
