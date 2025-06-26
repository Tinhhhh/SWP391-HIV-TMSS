package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.TreatmentRegimenRequest;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenResponse;

public interface TreatmentRegimenService {

    ListResponse getAllTreatmentRegimenByDoctor(int pageNo, int pageSize, String keyword, String sortBy, String sortDir);

    TreatmentRegimenResponse createTreatmentRegimen(TreatmentRegimenRequest request);

    TreatmentRegimenResponse getTreatmentRegimenById(Long id);

    TreatmentRegimenResponse updateTreatmentRegimen(Long id, TreatmentRegimenRequest request);

    void deleteTreatmentRegimen(Long id);

}
