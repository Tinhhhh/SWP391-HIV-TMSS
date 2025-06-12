package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.response.ListResponse;

public interface TreatmentRegimenService {

    ListResponse getAllTreatmentRegimenByDoctor(int pageNo, int pageSize, String keyword, String sortBy, String sortDir);

}
