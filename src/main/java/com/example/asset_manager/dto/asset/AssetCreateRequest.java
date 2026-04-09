package com.example.asset_manager.dto.asset;

import com.example.asset_manager.validation.NoJapaneseCharacters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AssetCreateRequest {

    @NotBlank(message = "Asset tag is required.")
    @Size(max = 255, message = "Asset tag must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String assetTag;

    @NotNull(message = "Serial number is required.")
    @Positive(message = "Serial number must be greater than 0.")
    private Long serialNumber;

    @NotBlank(message = "Model is required.")
    @Size(max = 255, message = "Model must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String model;

    @NotBlank(message = "CPU is required.")
    @Size(max = 255, message = "CPU must be 255 characters or fewer.")
    @NoJapaneseCharacters
    private String cpuAmt;

    @NotNull(message = "RAM is required.")
    @Positive(message = "RAM must be greater than 0.")
    private Integer ramAmt;

    @NotNull(message = "Storage is required.")
    @Positive(message = "Storage must be greater than 0.")
    private Integer storageAmt;

    @NotBlank(message = "Unit status is required.")
    @NoJapaneseCharacters
    private String unitStatus;

    @Size(max = 500, message = "Image URL must be 500 characters or fewer.")
    @NoJapaneseCharacters
    private String imageUrl;

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
}
