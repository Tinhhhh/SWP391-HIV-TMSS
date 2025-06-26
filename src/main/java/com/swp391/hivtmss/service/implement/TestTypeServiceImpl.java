package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.TestType;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.TestTypeRequest;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import com.swp391.hivtmss.repository.TestTypeRepository;
import com.swp391.hivtmss.service.TestTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTypeServiceImpl implements TestTypeService {

    private final TestTypeRepository testTypeRepository;

    @Override
    public List<TestTypeResponse> getAllTestType() {

        List<TestType> testTypes = testTypeRepository.findAll();
        return testTypes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void createTestType(TestTypeRequest testTypeRequest) {
        TestType testTypeID = testTypeRepository.findById(testTypeRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("testTypeID not found"));

        TestType testType = new TestType();

        testType.setName(testTypeRequest.getName());
        testType.setDescription(testTypeRequest.getDescription());
        testType.setCode(testTypeRequest.getCode());
        testType.setIsActive(ActiveStatus.ACTIVE);
        testType.setApplicable(testTypeRequest.getApplicable());

        testTypeRepository.save(testType);

    }

    @Override
    public TestTypeResponse getTestTypeByID(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("testTypeID not found"));
        return convertToResponse(testType);
    }

    @Override
    public void updateTestType(Long id, TestTypeRequest testTypeRequest) {
        TestType testTypeID = testTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("testTypeID not found"));

        TestType testType = new TestType();

        testType.setName(testTypeRequest.getName());
        testType.setDescription(testTypeRequest.getDescription());
        testType.setCode(testTypeRequest.getCode());
        testType.setApplicable(testTypeRequest.getApplicable());
        testTypeRepository.save(testType);
    }

    @Override
    public void deleteTestType(Long id, TestTypeRequest testTypeRequest) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("testTypeID not found"));

        testType.setIsActive(ActiveStatus.INACTIVE);
        testTypeRepository.save(testType);
    }

    public TestTypeResponse convertToResponse(TestType testType) {
        return new TestTypeResponse(
                testType.getId(),
                testType.getName(),
                testType.getCode(),
                testType.getDescription(),
                testType.getIsActive(),
                testType.getApplicable()

        );
    }
}
