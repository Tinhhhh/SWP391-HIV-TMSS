package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.TreatmentRegimenDrugRequest;
import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenDrugResponse;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;


public interface TreatmentRegimenDrugService {
    TreatmentRegimenDrugResponse create(Long treatmentRegimenId, TreatmentRegimenDrugRequest request);
    TreatmentRegimenDrugResponse getById(Long id);
    TreatmentRegimenDrugResponse update(Long id, TreatmentRegimenDrugRequest request);
    void delete(Long id);
    void deleteAllByTreatmentRegimen(Long treatmentRegimenId);

    Page<TreatmentRegimenDrugResponse> getAllByTreatmentRegimen(Long treatmentRegimenId, int pageNo, int pageSize, String sortBy, String sortDir);
    Page<TreatmentRegimenDrugResponse> getAllByTreatmentRegimenAndMethod(Long treatmentRegimenId, int method, int pageNo, int pageSize, String sortBy, String sortDir);
    Page<TreatmentRegimenDrugResponse> getAllByDrug(Long drugId, int pageNo, int pageSize, String sortBy, String sortDir);

    TreatmentRegimenDrugResponse deactivate(Long id);
    TreatmentRegimenDrugResponse activate(Long id);
}

