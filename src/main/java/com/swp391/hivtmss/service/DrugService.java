package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.DrugRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface DrugService{
    void createDrug(@Valid DrugRequest drugRequest);

    Object getDrugById(Long id);

    List<DrugResponse> getAllDrugs();

    void deleteDrug(Long id, @Valid DrugRequest drugRequest);
}
