package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Blog;
import com.swp391.hivtmss.model.entity.Drug;
import com.swp391.hivtmss.model.entity.TestType;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.DrugRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import com.swp391.hivtmss.repository.DrugRepository;
import com.swp391.hivtmss.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final DrugRepository drugRepository;

    @Override
    public void createDrug(DrugRequest drugRequest) {

        Drug drug = new Drug();
        drug.setName(drugRequest.getName());
        drug.setShortName(drugRequest.getShortName());
        drug.setType(drugRequest.getType());
        drug.setIsActive(ActiveStatus.ACTIVE);
        drug.setCreatedDate(new Date());

        Drug savedBlogDrug = drugRepository.save(drug);
        convertToResponse(savedBlogDrug);
    }

    @Override
    public DrugResponse getDrugById(Long id) {
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, Drug not found"));
        return convertToResponse(drug);
    }

    @Override
    public List<DrugResponse> getAllDrugs() {
        List<Drug> drugs = drugRepository.findAll();
        return drugs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDrug(Long id, DrugRequest drugRequest) {
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DrugID not found"));
        drug.setIsActive(ActiveStatus.INACTIVE);
        drugRepository.save(drug);
    }

    @Override
    public void updateDrug(Long id, DrugRequest drugRequest) {
        Drug drugId = drugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DrugID not found"));

        Drug drug = new Drug();
        drug.setName(drugRequest.getName());
        drug.setShortName(drugRequest.getShortName());
        drug.setType(drugRequest.getType());
        drug.setCreatedDate(new Date());

        drugRepository.save(drug);

    }

    public DrugResponse convertToResponse(Drug drug){
        return new DrugResponse(
                drug.getId(),
                drug.getName(),
                drug.getShortName(),
                drug.getType(),
                drug.getIsActive(),
                drug.getCreatedDate()
        );
    }
}
