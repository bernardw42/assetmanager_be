package com.example.asset_manager.service;

import com.example.asset_manager.dto.assignment.AssignmentCreateRequest;
import com.example.asset_manager.dto.request.*;
import com.example.asset_manager.entity.AssetRequest;
import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.AssetRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AssetRequestService {

    private final AssetRequestRepository requestRepository;
    private final EmployeeService employeeService;
    private final AssetAssignmentService assignmentService;

    public AssetRequestService(
            AssetRequestRepository requestRepository,
            EmployeeService employeeService,
            AssetAssignmentService assignmentService
    ) {
        this.requestRepository = requestRepository;
        this.employeeService = employeeService;
        this.assignmentService = assignmentService;
    }

    public AssetRequestResponse create(AssetRequestCreateRequest req) {
        // ensure employee exists
        validatePositive(req.getEmployeeId(), "Employee");
        var employee = employeeService.findEntity(req.getEmployeeId());
        if (Boolean.FALSE.equals(employee.getIsActive())) {
            throw new IllegalArgumentException("Inactive employees cannot submit requests.");
        }

        String requestType = normalizeRequestType(req.getRequestType());
        String minCpuAmt = normalizeOptionalText(req.getMinCpuAmt());
        String reason = normalizeOptionalText(req.getReason());

        AssetRequest r = new AssetRequest();
        r.setEmployeeId(req.getEmployeeId());
        r.setRequestType(requestType);
        r.setMinCpuAmt(minCpuAmt);
        r.setMinRamAmt(req.getMinRamAmt());
        r.setMinStorageAmt(req.getMinStorageAmt());
        r.setReason(reason);
        r.setUnitStatus("PENDING");
        return toResponse(requestRepository.save(r));
    }

    public List<AssetRequestResponse> listAll() {
        return requestRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<AssetRequestResponse> listByStatus(String status) {
        return requestRepository.findByUnitStatus(normalizeRequestStatus(status)).stream().map(this::toResponse).toList();
    }

    public List<AssetRequestResponse> listByEmployee(Long employeeId) {
        return requestRepository.findByEmployeeId(employeeId).stream().map(this::toResponse).toList();
    }

    public AssetRequestResponse review(AssetRequestReviewRequest req) {
        validatePositive(req.getRequestId(), "Request");
        validatePositive(req.getReviewedByEmployeeId(), "Reviewing admin");
        AssetRequest r = find(req.getRequestId());

        if (!"PENDING".equals(r.getUnitStatus())) {
            throw new IllegalStateException("Only PENDING requests can be reviewed. Current=" + r.getUnitStatus());
        }

        var reviewer = employeeService.findEntity(req.getReviewedByEmployeeId());
        if (Boolean.FALSE.equals(reviewer.getIsActive())) {
            throw new IllegalArgumentException("Reviewing admin must be active.");
        }

        String decision = normalizeDecision(req.getDecision());

        r.setUnitStatus(decision);
        r.setReviewedBy(req.getReviewedByEmployeeId());
        r.setReviewedAt(LocalDateTime.now());
        r.setReviewComment(normalizeOptionalText(req.getReviewComment()));

        return toResponse(requestRepository.save(r));
    }

    public AssetRequestResponse fulfill(AssetRequestFulfillRequest req) {
        validatePositive(req.getRequestId(), "Request");
        validatePositive(req.getAssetId(), "Asset");
        validatePositive(req.getReviewedByEmployeeId(), "Reviewing admin");
        AssetRequest r = find(req.getRequestId());

        if (!"APPROVED".equals(r.getUnitStatus())) {
            throw new IllegalStateException("Only APPROVED requests can be fulfilled. Current=" + r.getUnitStatus());
        }

        var reviewer = employeeService.findEntity(req.getReviewedByEmployeeId());
        if (Boolean.FALSE.equals(reviewer.getIsActive())) {
            throw new IllegalArgumentException("Reviewing admin must be active.");
        }

        // create assignment
        AssignmentCreateRequest assignReq = new AssignmentCreateRequest();
        assignReq.setAssetId(req.getAssetId());
        assignReq.setEmployeeId(r.getEmployeeId());
        assignReq.setApprovedBy(req.getReviewedByEmployeeId());
        assignReq.setNote(normalizeOptionalText(req.getNote()));

        assignmentService.assign(assignReq);

        r.setRequestAssetId(req.getAssetId());
        r.setReviewedBy(req.getReviewedByEmployeeId());
        r.setReviewedAt(LocalDateTime.now());
        r.setUnitStatus("FULFILLED");

        return toResponse(requestRepository.save(r));
    }

    private AssetRequest find(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found: " + id));
    }

    private AssetRequestResponse toResponse(AssetRequest r) {
        AssetRequestResponse out = new AssetRequestResponse();
        out.setId(r.getId());
        out.setEmployeeId(r.getEmployeeId());
        out.setRequestType(r.getRequestType());
        out.setMinCpuAmt(r.getMinCpuAmt());
        out.setMinRamAmt(r.getMinRamAmt());
        out.setMinStorageAmt(r.getMinStorageAmt());
        out.setReason(r.getReason());
        out.setUnitStatus(r.getUnitStatus());
        out.setReviewedBy(r.getReviewedBy());
        out.setReviewedAt(r.getReviewedAt());
        out.setReviewComment(r.getReviewComment());
        out.setRequestAssetId(r.getRequestAssetId());
        out.setCreatedAt(r.getCreatedAt());
        out.setUpdatedAt(r.getUpdatedAt());
        return out;
    }

    private void validatePositive(Long value, String label) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(label + " is required.");
        }
    }

    private String normalizeRequestType(String requestType) {
        String normalized = requireText(requestType, "Request type").toUpperCase();
        if (!Set.of("LOAN", "EXCHANGE").contains(normalized)) {
            throw new IllegalArgumentException("Request type must be LOAN or EXCHANGE.");
        }
        return normalized;
    }

    private String normalizeDecision(String decision) {
        String normalized = requireText(decision, "Decision").toUpperCase();
        if (!Set.of("APPROVED", "REJECTED").contains(normalized)) {
            throw new IllegalArgumentException("Decision must be APPROVED or REJECTED.");
        }
        return normalized;
    }

    private String normalizeRequestStatus(String status) {
        String normalized = requireText(status, "Status").toUpperCase();
        if (!Set.of("PENDING", "APPROVED", "REJECTED", "FULFILLED").contains(normalized)) {
            throw new IllegalArgumentException("Status must be PENDING, APPROVED, REJECTED, or FULFILLED.");
        }
        return normalized;
    }

    private String normalizeOptionalText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " is required.");
        }
        return value.trim();
    }
}
