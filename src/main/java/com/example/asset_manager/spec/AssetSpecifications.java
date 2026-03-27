package com.example.asset_manager.spec;

import com.example.asset_manager.dto.asset.AssetFilter;
import com.example.asset_manager.entity.Asset;
import org.springframework.data.jpa.domain.Specification;

public class AssetSpecifications {

    public static Specification<Asset> byFilter(AssetFilter f) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (f.getModel() != null && !f.getModel().isBlank()) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("model")), "%" + f.getModel().toLowerCase() + "%"));
            }

            if (f.getUnitStatus() != null && !f.getUnitStatus().isBlank()) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("unitStatus"), f.getUnitStatus()));
            }

            if (f.getMinRam() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("ramAmt"), f.getMinRam()));
            }

            if (f.getMinStorage() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("storageAmt"), f.getMinStorage()));
            }

            return predicates;
        };
    }
}
