package com.example.asset_manager.controller;

import com.example.asset_manager.dto.assignment.AssignmentCreateRequest;
import com.example.asset_manager.dto.assignment.AssignmentHistoryResponse;
import com.example.asset_manager.dto.assignment.AssignmentResponse;
import com.example.asset_manager.dto.assignment.AssignmentReturnRequest;
import com.example.asset_manager.service.AssetAssignmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;

    public AssetAssignmentController(AssetAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @PostMapping("/assign")
    public AssignmentResponse assign(@Valid @RequestBody AssignmentCreateRequest req) {
        return assignmentService.assign(req);
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @PostMapping("/return")
    public AssignmentResponse returnAsset(@Valid @RequestBody AssignmentReturnRequest req) {
        return assignmentService.returnAsset(req);
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @GetMapping("/current")
    public List<AssignmentResponse> current() {
        return assignmentService.listCurrent();
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @GetMapping("/employee/{employeeId}/current")
    public List<AssignmentResponse> currentByEmployee(@PathVariable Long employeeId) {
        return assignmentService.listCurrentByEmployee(employeeId);
    }

    // NEW: lending history list for an asset
    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @GetMapping("/asset/{assetId}/history")
    public List<AssignmentHistoryResponse> historyByAsset(@PathVariable Long assetId) {
        return assignmentService.listHistoryByAsset(assetId);
    }
}
