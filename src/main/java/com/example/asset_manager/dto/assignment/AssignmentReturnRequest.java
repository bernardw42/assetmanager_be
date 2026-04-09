package com.example.asset_manager.dto.assignment;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AssignmentReturnRequest {

    @NotNull(message = "Assignment is required.")
    private Long assignmentId;

    @Size(max = 1000, message = "Note must be 1000 characters or fewer.")
    @NoJapaneseCharacters
    private String note;

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
