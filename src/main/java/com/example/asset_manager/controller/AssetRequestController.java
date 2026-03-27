package com.example.asset_manager.controller;

import com.example.asset_manager.dto.request.*;
import com.example.asset_manager.service.AssetRequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class AssetRequestController {

    private final AssetRequestService requestService;

    public AssetRequestController(AssetRequestService requestService) {
        this.requestService = requestService;
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @GetMapping
    public List<AssetRequestResponse> list(@RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) {
            return requestService.listAll();
        }
        return requestService.listByStatus(status);
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @PostMapping("/review")
    public AssetRequestResponse review(@Valid @RequestBody AssetRequestReviewRequest req) {
        return requestService.review(req);
    }

    @PreAuthorize("hasAnyRole('ASSET_ADMIN','SYSTEM_ADMIN')")
    @PostMapping("/fulfill")
    public AssetRequestResponse fulfill(@Valid @RequestBody AssetRequestFulfillRequest req) {
        return requestService.fulfill(req);
    }
}
