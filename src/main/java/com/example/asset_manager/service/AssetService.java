package com.example.asset_manager.service;

import com.example.asset_manager.dto.asset.*;
import com.example.asset_manager.entity.Asset;
import com.example.asset_manager.exception.ConflictException;
import com.example.asset_manager.exception.NotFoundException;
import com.example.asset_manager.repository.AssetRepository;
import com.example.asset_manager.spec.AssetSpecifications;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> list(AssetFilter filter) {
        var spec = AssetSpecifications.byFilter(filter);
        return assetRepository.findAll(spec).stream().map(this::toResponse).toList();
    }

    public AssetResponse get(Long id) {
        return toResponse(findEntity(id));
    }

    public AssetResponse create(AssetCreateRequest req) {
        String assetTag = normalizeRequiredText(req.getAssetTag(), "Asset tag");
        String model = normalizeRequiredText(req.getModel(), "Model");
        String cpuAmt = normalizeRequiredText(req.getCpuAmt(), "CPU");
        String unitStatus = normalizeUnitStatus(req.getUnitStatus());
        validatePositive(req.getSerialNumber(), "Serial number");
        validatePositive(req.getRamAmt(), "RAM");
        validatePositive(req.getStorageAmt(), "Storage");
        ensureUniqueForCreate(assetTag, req.getSerialNumber());

        Asset a = new Asset();
        a.setAssetTag(assetTag);
        a.setSerialNumber(req.getSerialNumber());
        a.setModel(model);
        a.setCpuAmt(cpuAmt);
        a.setRamAmt(req.getRamAmt());
        a.setStorageAmt(req.getStorageAmt());
        a.setUnitStatus(unitStatus);
        a.setImageUrl(normalizeImageUrl(req.getImageUrl()));
        return toResponse(saveAsset(a));
    }

    public AssetResponse update(Long id, AssetUpdateRequest req) {
        Asset a = findEntity(id);
        String assetTag = normalizeRequiredText(req.getAssetTag(), "Asset tag");
        String model = normalizeRequiredText(req.getModel(), "Model");
        String cpuAmt = normalizeRequiredText(req.getCpuAmt(), "CPU");
        String unitStatus = normalizeUnitStatus(req.getUnitStatus());
        validatePositive(req.getSerialNumber(), "Serial number");
        validatePositive(req.getRamAmt(), "RAM");
        validatePositive(req.getStorageAmt(), "Storage");
        ensureUniqueForUpdate(id, assetTag, req.getSerialNumber());

        a.setAssetTag(assetTag);
        a.setSerialNumber(req.getSerialNumber());
        a.setModel(model);
        a.setCpuAmt(cpuAmt);
        a.setRamAmt(req.getRamAmt());
        a.setStorageAmt(req.getStorageAmt());
        a.setUnitStatus(unitStatus);
        a.setImageUrl(normalizeImageUrl(req.getImageUrl()));
        return toResponse(saveAsset(a));
    }

    public void delete(Long id) {
        Asset a = findEntity(id);
        assetRepository.delete(a);
    }

    private Asset findEntity(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Asset not found: " + id));
    }

    private AssetResponse toResponse(Asset a) {
        AssetResponse r = new AssetResponse();
        r.setId(a.getId());
        r.setAssetTag(a.getAssetTag());
        r.setSerialNumber(a.getSerialNumber());
        r.setModel(a.getModel());
        r.setCpuAmt(a.getCpuAmt());
        r.setRamAmt(a.getRamAmt());
        r.setStorageAmt(a.getStorageAmt());
        r.setUnitStatus(a.getUnitStatus());
        r.setImageUrl(a.getImageUrl());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }

    private String normalizeImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }

        String trimmed = imageUrl.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeRequiredText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        return value.trim();
    }

    private String normalizeUnitStatus(String unitStatus) {
        String normalized = normalizeRequiredText(unitStatus, "Unit status").toUpperCase();
        Set<String> allowed = Set.of("AVAILABLE", "ASSIGNED", "REPAIR", "RETIRED");
        if (!allowed.contains(normalized)) {
            throw new IllegalArgumentException("Unit status must be one of AVAILABLE, ASSIGNED, REPAIR, or RETIRED.");
        }

        return normalized;
    }

    private void validatePositive(Number value, String fieldName) {
        if (value == null || value.longValue() <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
    }

    private void ensureUniqueForCreate(String assetTag, Long serialNumber) {
        var fieldErrors = new LinkedHashMap<String, String>();
        if (assetRepository.existsByAssetTagIgnoreCase(assetTag)) {
            fieldErrors.put("assetTag", "Asset tag already exists, including retired assets.");
        }
        if (assetRepository.existsBySerialNumber(serialNumber)) {
            fieldErrors.put("serialNumber", "Serial number already exists, including retired assets.");
        }
        if (!fieldErrors.isEmpty()) {
            throw new ConflictException("Validation failed", fieldErrors);
        }
    }

    private void ensureUniqueForUpdate(Long id, String assetTag, Long serialNumber) {
        var fieldErrors = new LinkedHashMap<String, String>();
        if (assetRepository.existsByAssetTagIgnoreCaseAndIdNot(assetTag, id)) {
            fieldErrors.put("assetTag", "Asset tag already exists, including retired assets.");
        }
        if (assetRepository.existsBySerialNumberAndIdNot(serialNumber, id)) {
            fieldErrors.put("serialNumber", "Serial number already exists, including retired assets.");
        }
        if (!fieldErrors.isEmpty()) {
            throw new ConflictException("Validation failed", fieldErrors);
        }
    }

    private Asset saveAsset(Asset asset) {
        try {
            return assetRepository.save(asset);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Asset tag or serial number already exists.");
        }
    }
}
