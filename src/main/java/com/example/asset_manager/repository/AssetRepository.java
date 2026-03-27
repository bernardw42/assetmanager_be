package com.example.asset_manager.repository;

import com.example.asset_manager.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    boolean existsByAssetTagIgnoreCase(String assetTag);
    boolean existsByAssetTagIgnoreCaseAndIdNot(String assetTag, Long id);
    boolean existsBySerialNumber(Long serialNumber);
    boolean existsBySerialNumberAndIdNot(Long serialNumber, Long id);
}
