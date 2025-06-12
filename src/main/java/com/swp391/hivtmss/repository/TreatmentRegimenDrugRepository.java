package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRegimenDrugRepository extends JpaRepository<TreatmentRegimenDrug, Long> {

    List<TreatmentRegimenDrug> findByTreatmentRegimen_Id(Long treatmentRegimenId);

    List<TreatmentRegimenDrug> findByTreatmentRegimen_IdAndMethod(Long treatmentRegimenId, int method);
}
