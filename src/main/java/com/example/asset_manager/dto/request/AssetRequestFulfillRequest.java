package com.example.asset_manager.dto.request;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AssetRequestFulfillRequest {

    @NotNull(message = "Request is required.")
    private Long requestId;

    @NotNull(message = "Asset is required.")
    private Long assetId;

    @NotNull(message = "Reviewing admin is required.")
    private Long reviewedByEmployeeId; // asset admin id

    @Size(max = 1000, message = "Note must be 1000 characters or fewer.")
    @NoJapaneseCharacters
    private String note;

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Long getReviewedByEmployeeId() { return reviewedByEmployeeId; }
    public void setReviewedByEmployeeId(Long reviewedByEmployeeId) { this.reviewedByEmployeeId = reviewedByEmployeeId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
