package com.example.asset_manager.repository;

import com.example.asset_manager.entity.AssetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRequestRepository extends JpaRepository<AssetRequest, Long> {
    List<AssetRequest> findByUnitStatus(String unitStatus);
    List<AssetRequest> findByEmployeeId(Long employeeId);
    Optional<AssetRequest> findFirstByEmployeeIdAndRequestAssetIdAndUnitStatusOrderByUpdatedAtDesc(Long employeeId, Long requestAssetId, String unitStatus);
}
