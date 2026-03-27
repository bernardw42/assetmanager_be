package com.example.asset_manager.controller;

import com.example.asset_manager.dto.request.AssetRequestCreateRequest;
import com.example.asset_manager.dto.request.AssetRequestResponse;
import com.example.asset_manager.service.AssetRequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-requests")
public class EmployeeRequestController {

    private final AssetRequestService requestService;

    public EmployeeRequestController(AssetRequestService requestService) {
        this.requestService = requestService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public AssetRequestResponse create(@Valid @RequestBody AssetRequestCreateRequest req) {
        return requestService.create(req);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{employeeId}")
    public List<AssetRequestResponse> myRequests(@PathVariable Long employeeId) {
        return requestService.listByEmployee(employeeId);
    }
}
