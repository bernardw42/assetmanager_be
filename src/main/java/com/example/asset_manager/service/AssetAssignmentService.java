package com.example.asset_manager.service;

import com.example.asset_manager.dto.assignment.AssignmentCreateRequest;
import com.example.asset_manager.dto.assignment.AssignmentHistoryResponse;
import com.example.asset_manager.dto.assignment.AssignmentResponse;
import com.example.asset_manager.dto.assignment.AssignmentReturnRequest;
import com.example.asset_manager.entity.Asset;
import com.example.asset_manager.entity.AssetAssignment;
import com.example.asset_manager.entity.AssetRequest;
import com.example.asset_manager.entity.Employee;
import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.AssetAssignmentRepository;
import com.example.asset_manager.repository.AssetRequestRepository;
import com.example.asset_manager.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetAssignmentService {

    private final AssetAssignmentRepository assignmentRepository;
    private final AssetRepository assetRepository;
    private final AssetRequestRepository requestRepository;
    private final EmployeeService employeeService;

    public AssetAssignmentService(
            AssetAssignmentRepository assignmentRepository,
            AssetRepository assetRepository,
            AssetRequestRepository requestRepository,
            EmployeeService employeeService
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assetRepository = assetRepository;
        this.requestRepository = requestRepository;
        this.employeeService = employeeService;
    }

    public List<AssignmentResponse> listCurrent() {
        return assignmentRepository.findByReturnedAtIsNullOrderByAssignedAtDesc()
                .stream()
                .map(this::toResponseWithJoin)
                .toList();
    }

    public List<AssignmentResponse> listCurrentByEmployee(Long employeeId) {
        return assignmentRepository.findByEmployeeIdAndReturnedAtIsNull(employeeId)
                .stream()
                .map(this::toResponseWithJoin)
                .toList();
    }

    @Transactional
    public AssignmentResponse assign(AssignmentCreateRequest req) {
        validatePositive(req.getAssetId(), "Asset");
        validatePositive(req.getApprovedBy(), "Approving admin");
        String note = normalizeNote(req.getNote());

        Asset asset = assetRepository.findById(req.getAssetId())
                .orElseThrow(() -> new NotFoundException("Asset not found: " + req.getAssetId()));

        AssetRequest linkedRequest = null;
        Long borrowerId = req.getEmployeeId();
        if (req.getRequestId() != null) {
            linkedRequest = requestRepository.findById(req.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Request not found: " + req.getRequestId()));

            if (!"APPROVED".equals(linkedRequest.getUnitStatus())) {
                throw new IllegalStateException("Only APPROVED requests can be assigned from the assignments page. Current=" + linkedRequest.getUnitStatus());
            }

            borrowerId = linkedRequest.getEmployeeId();
        }

        validatePositive(borrowerId, "Employee");

        Employee borrower = employeeService.findEntity(borrowerId);
        Employee lender = employeeService.findEntity(req.getApprovedBy());

        if (Boolean.FALSE.equals(borrower.getIsActive())) {
            throw new IllegalArgumentException("Cannot assign assets to an inactive employee.");
        }
        if (Boolean.FALSE.equals(lender.getIsActive())) {
            throw new IllegalArgumentException("Approving admin must be active.");
        }
        if (!"AVAILABLE".equalsIgnoreCase(asset.getUnitStatus())) {
            throw new IllegalStateException("Only AVAILABLE assets can be assigned. Current=" + asset.getUnitStatus());
        }

        assignmentRepository.findFirstByAssetIdAndReturnedAtIsNull(asset.getId())
                .ifPresent(a -> { throw new IllegalStateException("Asset is already assigned: " + asset.getId()); });

        AssetAssignment aa = new AssetAssignment();
        aa.setAssetId(asset.getId());
        aa.setEmployeeId(borrower.getId());
        aa.setApprovedBy(lender.getId());
        aa.setAssignedAt(LocalDateTime.now());
        aa.setReturnedAt(null);
        aa.setNote(note);

        AssetAssignment saved = assignmentRepository.save(aa);

        asset.setUnitStatus("ASSIGNED");
        assetRepository.save(asset);

        if (linkedRequest != null) {
            linkedRequest.setRequestAssetId(asset.getId());
            linkedRequest.setReviewedBy(req.getApprovedBy());
            linkedRequest.setReviewedAt(LocalDateTime.now());
            linkedRequest.setUnitStatus("FULFILLED");
            requestRepository.save(linkedRequest);
        }

        return toResponseWithJoin(saved);
    }

    @Transactional
    public AssignmentResponse returnAsset(AssignmentReturnRequest req) {
        validatePositive(req.getAssignmentId(), "Assignment");
        String note = normalizeNote(req.getNote());

        AssetAssignment aa = assignmentRepository.findById(req.getAssignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment not found: " + req.getAssignmentId()));

        if (aa.getReturnedAt() != null) {
            throw new IllegalStateException("Assignment already returned: " + aa.getId());
        }

        aa.setReturnedAt(LocalDateTime.now());
        if (note != null) {
            aa.setNote(note);
        }

        AssetAssignment saved = assignmentRepository.save(aa);

        Asset asset = assetRepository.findById(saved.getAssetId())
                .orElseThrow(() -> new NotFoundException("Asset not found: " + saved.getAssetId()));
        asset.setUnitStatus("AVAILABLE");
        assetRepository.save(asset);

        return toResponseWithJoin(saved);
    }

    // ===== NEW: history by assetId =====
    public List<AssignmentHistoryResponse> listHistoryByAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new NotFoundException("Asset not found: " + assetId));

        return assignmentRepository.findByAssetIdOrderByAssignedAtDesc(asset.getId())
                .stream()
                .map(this::toHistoryResponseWithJoin)
                .toList();
    }

    private AssignmentResponse toResponseWithJoin(AssetAssignment aa) {
        AssignmentResponse r = new AssignmentResponse();
        r.setId(aa.getId());
        r.setAssetId(aa.getAssetId());
        r.setEmployeeId(aa.getEmployeeId());
        r.setAssignedAt(aa.getAssignedAt());
        r.setReturnedAt(aa.getReturnedAt());
        r.setNote(aa.getNote());

        Asset asset = assetRepository.findById(aa.getAssetId()).orElse(null);
        if (asset != null) {
            r.setAssetTag(asset.getAssetTag());
            r.setAssetModel(asset.getModel());
        }

        Employee borrower = employeeService.findEntity(aa.getEmployeeId());
        r.setEmployeeCode(borrower.getEmployeeId());
        r.setEmployeeName(borrower.getFullName());

        requestRepository.findFirstByEmployeeIdAndRequestAssetIdAndUnitStatusOrderByUpdatedAtDesc(
                aa.getEmployeeId(), aa.getAssetId(), "FULFILLED"
        ).ifPresent(request -> r.setRequestId(request.getId()));

        return r;
    }

    private AssignmentHistoryResponse toHistoryResponseWithJoin(AssetAssignment aa) {
        AssignmentHistoryResponse r = new AssignmentHistoryResponse();
        r.setId(aa.getId());

        r.setAssetId(aa.getAssetId());
        r.setBorrowerEmployeeId(aa.getEmployeeId());

        r.setAssignedAt(aa.getAssignedAt());
        r.setReturnedAt(aa.getReturnedAt());
        r.setNote(aa.getNote());

        Asset asset = assetRepository.findById(aa.getAssetId()).orElse(null);
        if (asset != null) {
            r.setAssetTag(asset.getAssetTag());
            r.setAssetModel(asset.getModel());
        }

        Employee borrower = employeeService.findEntity(aa.getEmployeeId());
        r.setBorrowerEmployeeCode(borrower.getEmployeeId());
        r.setBorrowerEmployeeName(borrower.getFullName());

        if (aa.getApprovedBy() != null) {
            Employee lender = employeeService.findEntity(aa.getApprovedBy());
            r.setLenderEmployeeId(lender.getId());
            r.setLenderEmployeeCode(lender.getEmployeeId());
            r.setLenderEmployeeName(lender.getFullName());
        }

        if (aa.getReturnedAt() != null && aa.getAssignedAt() != null) {
            long days = Duration.between(aa.getAssignedAt(), aa.getReturnedAt()).toDays();
            r.setDaysBorrowed((int) days);
        } else {
            r.setDaysBorrowed(null);
        }

        return r;
    }

    private void validatePositive(Long value, String label) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(label + " is required.");
        }
    }

    private String normalizeNote(String note) {
        if (note == null) {
            return null;
        }
        String trimmed = note.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
