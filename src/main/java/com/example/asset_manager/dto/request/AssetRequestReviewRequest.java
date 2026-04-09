package com.example.asset_manager.dto.request;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AssetRequestReviewRequest {

    @NotNull(message = "Request is required.")
    private Long requestId;

    @NotNull(message = "Reviewing admin is required.")
    private Long reviewedByEmployeeId;

    @NotBlank(message = "Decision is required.")
    @Pattern(regexp = "^(APPROVED|REJECTED)$", message = "Decision must be APPROVED or REJECTED.")
    @NoJapaneseCharacters
    private String decision; // APPROVED / REJECTED

    @Size(max = 1000, message = "Review comment must be 1000 characters or fewer.")
    @NoJapaneseCharacters
    private String reviewComment;

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public Long getReviewedByEmployeeId() { return reviewedByEmployeeId; }
    public void setReviewedByEmployeeId(Long reviewedByEmployeeId) { this.reviewedByEmployeeId = reviewedByEmployeeId; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
}
