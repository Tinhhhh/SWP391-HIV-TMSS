package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import com.swp391.hivtmss.repository.TestTypeRepository;
import com.swp391.hivtmss.service.TestTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestTypeServiceImpl implements TestTypeService {

    private final TestTypeRepository testTypeRepository;

    @Override
    public List<TestTypeResponse> getAllTestType() {
        return List.of();
    }
}
