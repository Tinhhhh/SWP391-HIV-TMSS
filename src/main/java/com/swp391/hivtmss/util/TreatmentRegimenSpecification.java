package com.swp391.hivtmss.util;

import com.swp391.hivtmss.model.entity.TreatmentRegimen;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class TreatmentRegimenSpecification {

    public Specification<TreatmentRegimen> hasName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), likePattern);
        };
    }

    public Specification<TreatmentRegimen> hasApplicable(String applicable) {
        return (root, query, cb) -> {
            if (applicable == null || applicable.trim().isEmpty()) {
                return null;
            }

            String likePattern = "%" + applicable.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("applicable")), likePattern);
        };
    }

    public Specification<TreatmentRegimen> hasActive(ActiveStatus active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }
            return cb.equal(root.get("isActive"), active);
        };
    }

}
