package com.swp391.hivtmss.service;


import com.swp391.hivtmss.model.payload.request.TestTypeRequest;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface TestTypeService {
    List<TestTypeResponse> getAllTestType();

    void createTestType(TestTypeRequest testTypeRequest);

    TestTypeResponse getTestTypeByID(Long id);

    void updateTestType(Long id, @Valid TestTypeRequest testTypeRequest);

    void deleteTestType(Long id, @Valid TestTypeRequest testTypeRequest);
}
