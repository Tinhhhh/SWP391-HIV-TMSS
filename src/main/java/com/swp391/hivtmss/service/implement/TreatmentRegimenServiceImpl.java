package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Drug;
import com.swp391.hivtmss.model.entity.TreatmentRegimen;
import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.TreatmentRegimenDrugRequest;
import com.swp391.hivtmss.model.payload.request.TreatmentRegimenRequest;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenDrugResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenResponse;
import com.swp391.hivtmss.repository.DrugRepository;
import com.swp391.hivtmss.repository.TreatmentRegimenDrugRepository;
import com.swp391.hivtmss.repository.TreatmentRegimenRepository;
import com.swp391.hivtmss.service.TreatmentRegimenService;
import com.swp391.hivtmss.util.TreatmentRegimenSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentRegimenServiceImpl implements TreatmentRegimenService {

    private final TreatmentRegimenRepository treatmentRegimenRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;
    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;
    private final DrugRepository drugRepository;

    @Override
    public ListResponse getAllTreatmentRegimenByDoctor(int pageNo, int pageSize, String keyword, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<TreatmentRegimen> spec = Specification.where(TreatmentRegimenSpecification.hasName(keyword)
                .or(TreatmentRegimenSpecification.hasApplicable(keyword))
                .or(TreatmentRegimenSpecification.hasActive(ActiveStatus.ACTIVE)));

        Page<TreatmentRegimen> regimenDetailPage = treatmentRegimenRepository.findAll(spec, pageable);
        List<TreatmentRegimen> resource = regimenDetailPage.getContent();
        List<TreatmentRegimenResponse> modelResponse = new ArrayList<>();

        for (TreatmentRegimen treatmentRegimen : resource) {
            TreatmentRegimenResponse treatmentRegimenResponse = modelMapper.map(treatmentRegimen, TreatmentRegimenResponse.class);

            // Lấy danh sách thuốc theo phác đồ điều trị
            Map<Integer, List<Drug>> drugMap = new HashMap<>();

            List<TreatmentRegimenDrug> treatmentRegimenDrugs = treatmentRegimenDrugRepository
                    .findByTreatmentRegimen_Id(treatmentRegimen.getId());

            if (!treatmentRegimenDrugs.isEmpty()) {
                // Nhóm theo method
                Map<Integer, List<TreatmentRegimenDrug>> treatmentRegimenDrugResponses = treatmentRegimenDrugs.stream()
                        .map(treatmentRegimenDrug -> {

                            drugMap.computeIfAbsent(treatmentRegimenDrug.getMethod(), k -> new ArrayList<>())
                                    .add(drugRepository.findById(treatmentRegimenDrug.getDrug().getId())
                                            .orElseThrow(() -> new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Request fails, drug not found")));

                            return treatmentRegimenDrug;
                        }).collect(Collectors.groupingBy(TreatmentRegimenDrug::getMethod));


                // Chuyển đổi từng nhóm thành DTO
                List<TreatmentRegimenDrugResponse> drugMethods = treatmentRegimenDrugResponses.entrySet().stream()
                        .map(entry -> {
                            Integer method = entry.getKey();

                            List<Drug> drugsOfMethod = drugMap.getOrDefault(method, new ArrayList<>());
                            List<DrugResponse> drugResponses = drugsOfMethod.stream()
                                    .map(drug -> modelMapper.map(drug, DrugResponse.class))
                                    .toList();

                            TreatmentRegimenDrugResponse drugMethodResponse = new TreatmentRegimenDrugResponse();
                            drugMethodResponse.setMethod(method);
                            drugMethodResponse.setDrugs(drugResponses);
                            return drugMethodResponse;
                        })
                        .toList();

                treatmentRegimenResponse.setDrugMethods(drugMethods);
            }

            modelResponse.add(treatmentRegimenResponse);
        }

        return new ListResponse(modelResponse,
                pageNo,
                pageSize,
                regimenDetailPage.getTotalElements(),
                regimenDetailPage.getTotalPages(),
                regimenDetailPage.isLast());
    }

    @Override
    public TreatmentRegimenResponse createTreatmentRegimen(TreatmentRegimenRequest request) {
        if (treatmentRegimenRepository.existsByName(request.getName())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Treatment regimen with this name already exists");
        }

        TreatmentRegimen treatmentRegimen = new TreatmentRegimen();
        treatmentRegimen.setName(request.getName());
        treatmentRegimen.setApplicable(request.getApplicable());
        treatmentRegimen.setLineLevel(request.getLineLevel());
        treatmentRegimen.setNote(request.getNote());
        treatmentRegimen.setIsActive(ActiveStatus.ACTIVE);

        TreatmentRegimen savedRegimen = treatmentRegimenRepository.save(treatmentRegimen);

        if (request.getTreatmentRegimenDrugs() != null && !request.getTreatmentRegimenDrugs().isEmpty()) {
            for (TreatmentRegimenDrugRequest drugRequest : request.getTreatmentRegimenDrugs()) {
                Drug drug = drugRepository.findById(drugRequest.getDrugId())
                        .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Drug not found"));

                TreatmentRegimenDrug treatmentRegimenDrug = new TreatmentRegimenDrug();
                treatmentRegimenDrug.setDrug(drug);
                treatmentRegimenDrug.setMethod(drugRequest.getMethod());
                treatmentRegimenDrug.setTreatmentRegimen(savedRegimen);
                treatmentRegimenDrugRepository.save(treatmentRegimenDrug);
            }
        }

        return modelMapper.map(savedRegimen, TreatmentRegimenResponse.class);
    }


    @Override
    public TreatmentRegimenResponse getTreatmentRegimenById(Long id) {
        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Treatment regimen not found"));
        return modelMapper.map(treatmentRegimen, TreatmentRegimenResponse.class);
    }

    @Override
    @Transactional
    public TreatmentRegimenResponse updateTreatmentRegimen(Long id, TreatmentRegimenRequest request) {
        TreatmentRegimen existingRegimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Treatment regimen not found"));

        // Update basic information
        existingRegimen.setName(request.getName());
        existingRegimen.setApplicable(request.getApplicable());
        existingRegimen.setLineLevel(request.getLineLevel());
        existingRegimen.setNote(request.getNote());

        // Update drug associations if provided
        if (request.getTreatmentRegimenDrugs() != null && !request.getTreatmentRegimenDrugs().isEmpty()) {
            // Clear only the associations in treatment_regimen_drug table
            List<TreatmentRegimenDrug> existingAssociations = treatmentRegimenDrugRepository
                    .findByTreatmentRegimen_Id(existingRegimen.getId());
            treatmentRegimenDrugRepository.deleteAllInBatch(existingAssociations);

            // Create new associations
            List<TreatmentRegimenDrug> newAssociations = request.getTreatmentRegimenDrugs().stream()
                    .map(drugRequest -> {
                        Drug drug = drugRepository.findById(drugRequest.getDrugId())
                                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND,
                                        "Drug not found with ID: " + drugRequest.getDrugId()));

                        TreatmentRegimenDrug association = new TreatmentRegimenDrug();
                        association.setDrug(drug);
                        association.setMethod(drugRequest.getMethod());
                        association.setTreatmentRegimen(existingRegimen);
                        return association;
                    })
                    .collect(Collectors.toList());

            // Save new associations in batch
            treatmentRegimenDrugRepository.saveAll(newAssociations);
        }

        TreatmentRegimen updatedRegimen = treatmentRegimenRepository.save(existingRegimen);
        return modelMapper.map(updatedRegimen, TreatmentRegimenResponse.class);
    }




    @Override
    public void deleteTreatmentRegimen(Long id) {
        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Treatment regimen not found"));
        treatmentRegimen.setIsActive(ActiveStatus.INACTIVE);
        treatmentRegimenRepository.save(treatmentRegimen);
    }

}
