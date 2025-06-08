package com.swp391.hivtmss.service;


import com.swp391.hivtmss.model.payload.request.TestTypeRequest;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;

import java.util.List;

public interface TestTypeService {

    List<TestTypeResponse> getAllTestType();

    void createTestType(TestTypeRequest testTypeRequest);

    Object getTestTypeByID(Long id);
}
