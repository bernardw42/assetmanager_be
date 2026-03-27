package com.example.asset_manager.controller;

import com.example.asset_manager.dto.asset.*;
import com.example.asset_manager.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<AssetResponse> list(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String unitStatus,
            @RequestParam(required = false) Integer minRam,
            @RequestParam(required = false) Integer minStorage
    ) {
        AssetFilter f = new AssetFilter();
        f.setModel(model);
        f.setUnitStatus(unitStatus);
        f.setMinRam(minRam);
        f.setMinStorage(minStorage);
        return assetService.list(f);
    }

    @GetMapping("/{id}")
    public AssetResponse get(@PathVariable Long id) {
        return assetService.get(id);
    }

    @PostMapping
    public AssetResponse create(@Valid @RequestBody AssetCreateRequest req) {
        return assetService.create(req);
    }

    @PutMapping("/{id}")
    public AssetResponse update(@PathVariable Long id, @Valid @RequestBody AssetUpdateRequest req) {
        return assetService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        assetService.delete(id);
    }
}
