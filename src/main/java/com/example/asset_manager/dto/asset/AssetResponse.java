package com.example.asset_manager.dto.asset;

import java.time.LocalDateTime;

public class AssetResponse {
    private Long id;
    private String assetTag;
    private Long serialNumber;
    private String model;
    private String cpuAmt;
    private Integer ramAmt;
    private Integer storageAmt;
    private String unitStatus;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAssetTag() { return assetTag; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }
    public Long getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Long serialNumber) { this.serialNumber = serialNumber; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getCpuAmt() { return cpuAmt; }
    public void setCpuAmt(String cpuAmt) { this.cpuAmt = cpuAmt; }
    public Integer getRamAmt() { return ramAmt; }
    public void setRamAmt(Integer ramAmt) { this.ramAmt = ramAmt; }
    public Integer getStorageAmt() { return storageAmt; }
    public void setStorageAmt(Integer storageAmt) { this.storageAmt = storageAmt; }
    public String getUnitStatus() { return unitStatus; }
    public void setUnitStatus(String unitStatus) { this.unitStatus = unitStatus; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
