package com.example.asset_manager.repository;

import com.example.asset_manager.entity.AssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Long> {
    Optional<AssetAssignment> findFirstByAssetIdAndReturnedAtIsNull(Long assetId);
    List<AssetAssignment> findByEmployeeIdAndReturnedAtIsNull(Long employeeId);
    List<AssetAssignment> findByReturnedAtIsNullOrderByAssignedAtDesc();

    // NEW: history for a specific asset (current + returned)
    List<AssetAssignment> findByAssetIdOrderByAssignedAtDesc(Long assetId);
}
