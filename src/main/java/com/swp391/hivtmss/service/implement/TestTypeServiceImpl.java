package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.payload.request.TestTypeRequest;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import com.swp391.hivtmss.service.TestTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTypeServiceImpl implements TestTypeService {
    @Override
    public List<TestTypeResponse> getAllTestType() {
        return List.of();
    }

    @Override
    public void createTestType(TestTypeRequest testTypeRequest) {

    }

    @Override
    public Object getTestTypeByID(Long id) {
        return null;
    }
}
