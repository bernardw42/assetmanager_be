package com.example.asset_manager.dto.asset;

public class AssetFilter {
    private String model;
    private String unitStatus;
    private Integer minRam;
    private Integer minStorage;

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getUnitStatus() { return unitStatus; }
    public void setUnitStatus(String unitStatus) { this.unitStatus = unitStatus; }
    public Integer getMinRam() { return minRam; }
    public void setMinRam(Integer minRam) { this.minRam = minRam; }
    public Integer getMinStorage() { return minStorage; }
    public void setMinStorage(Integer minStorage) { this.minStorage = minStorage; }
}
