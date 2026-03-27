package com.example.asset_manager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_tag", nullable = false, unique = true, length = 255)
    private String assetTag;

    // NOTE: your DB uses BIGINT for serial_number
    @Column(name = "serial_number", nullable = false, unique = true)
    private Long serialNumber;

    @Column(name = "model", nullable = false, length = 255)
    private String model;

    @Column(name = "cpu_amt", nullable = false, length = 255)
    private String cpuAmt;

    @Column(name = "ram_amt", nullable = false)
    private Integer ramAmt;

    @Column(name = "storage_amt", nullable = false)
    private Integer storageAmt;

    // NOTE: your DB uses VARCHAR for unit_status
    @Column(name = "unit_status", nullable = false, length = 255)
    private String unitStatus;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Asset() {}

    // getters/setters
    public Long getId() { return id; }
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
