package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.TreatmentRegimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRegimenRepository extends JpaRepository<TreatmentRegimen, Long>, JpaSpecificationExecutor<TreatmentRegimen> {
}
