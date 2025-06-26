package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface TreatmentRegimenDrugRepository extends JpaRepository<TreatmentRegimenDrug, Long> {

    List<TreatmentRegimenDrug> findByTreatmentRegimen_Id(Long treatmentRegimenId);

    List<TreatmentRegimenDrug> findByTreatmentRegimen_IdAndMethod(Long treatmentRegimenId, int method);

    List<TreatmentRegimenDrug> findByDrug_Id(Long drugId);

    boolean existsByDrugIdAndTreatmentRegimenIdAndMethod(Long drugId, Long treatmentRegimenId, int method);

    @Modifying
    @Query("DELETE FROM TreatmentRegimenDrug t WHERE t.treatmentRegimen.id = :treatmentRegimenId")
    void deleteByTreatmentRegimenId(@Param("treatmentRegimenId") Long treatmentRegimenId);

    Page<TreatmentRegimenDrug> findByTreatmentRegimen_Id(Long treatmentRegimenId, Pageable pageable);
    Page<TreatmentRegimenDrug> findByTreatmentRegimen_IdAndMethod(Long treatmentRegimenId, int method, Pageable pageable);
    Page<TreatmentRegimenDrug> findByDrug_Id(Long drugId, Pageable pageable);

}
