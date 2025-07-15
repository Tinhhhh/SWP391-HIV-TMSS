package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Drug;
import com.swp391.hivtmss.model.entity.TreatmentRegimen;
import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.MethodDrugsRequest;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentRegimenServiceImpl implements TreatmentRegimenService {

    private final TreatmentRegimenRepository treatmentRegimenRepository;
    private final ModelMapper modelMapper;
    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;
    private final DrugRepository drugRepository;

    @Override
    public ListResponse getAllTreatmentRegimenByDoctor(int pageNo, int pageSize, String keyword, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<TreatmentRegimen> spec = Specification.where(TreatmentRegimenSpecification.hasName(keyword))
                .or(TreatmentRegimenSpecification.hasApplicable(keyword))
                .or(TreatmentRegimenSpecification.hasActive(ActiveStatus.ACTIVE));

        Page<TreatmentRegimen> regimenDetailPage = treatmentRegimenRepository.findAll(spec, pageable);
        List<TreatmentRegimen> resource = regimenDetailPage.getContent();
        List<TreatmentRegimenResponse> modelResponse = new ArrayList<>();

        for (TreatmentRegimen treatmentRegimen : resource) {
            TreatmentRegimenResponse response = modelMapper.map(treatmentRegimen, TreatmentRegimenResponse.class);
            addDrugsToResponse(response, treatmentRegimen.getId());
            modelResponse.add(response);
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

        validateMethodsAndDrugs(request);

        TreatmentRegimen treatmentRegimen = new TreatmentRegimen();
        treatmentRegimen.setName(request.getName());
        treatmentRegimen.setApplicable(request.getApplicable());
        treatmentRegimen.setLineLevel(request.getLineLevel());
        treatmentRegimen.setNote(request.getNote());
        treatmentRegimen.setIsActive(ActiveStatus.ACTIVE);

        TreatmentRegimen savedRegimen = treatmentRegimenRepository.save(treatmentRegimen);

        if (request.getMethods() != null) {
            saveTreatmentRegimenDrugs(request.getMethods(), savedRegimen);
        }

        TreatmentRegimenResponse response = modelMapper.map(savedRegimen, TreatmentRegimenResponse.class);
        addDrugsToResponse(response, savedRegimen.getId());
        return response;
    }

    @Override
    public TreatmentRegimenResponse getTreatmentRegimenById(Long id) {
        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Treatment regimen not found"));
        TreatmentRegimenResponse response = modelMapper.map(treatmentRegimen, TreatmentRegimenResponse.class);
        addDrugsToResponse(response, id);
        return response;
    }

    @Override
    @Transactional
    public TreatmentRegimenResponse updateTreatmentRegimen(Long id, TreatmentRegimenRequest request) {
        TreatmentRegimen regimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Treatment Regimen not found with id: %d", id)));

        // Update the treatment regimen
        regimen.setName(request.getName());
        regimen.setApplicable(request.getApplicable());
        regimen.setLineLevel(request.getLineLevel());
        regimen.setNote(request.getNote());

        // Remove existing drugs
        treatmentRegimenDrugRepository.deleteByTreatmentRegimenId(id);

        // Save new drugs
        saveTreatmentRegimenDrugs(request.getMethods(), regimen);

        TreatmentRegimen savedRegimen = treatmentRegimenRepository.save(regimen);
        TreatmentRegimenResponse response = modelMapper.map(savedRegimen, TreatmentRegimenResponse.class);
        addDrugsToResponse(response, savedRegimen.getId());

        return response;
    }


    @Override
    public void deleteTreatmentRegimen(Long id) {
        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND, "Treatment regimen not found"));
        treatmentRegimen.setIsActive(ActiveStatus.INACTIVE);
        treatmentRegimenRepository.save(treatmentRegimen);
    }

    private void validateMethodsAndDrugs(TreatmentRegimenRequest request) {
        // Validate number of methods
        if (request.getMethods().size() != request.getNumberOfMethods()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Number of methods (%d) doesn't match the specified count (%d)",
                            request.getMethods().size(), request.getNumberOfMethods()));
        }

        // Validate method numbers are sequential
        Set<Integer> methodNumbers = request.getMethods().stream()
                .map(MethodDrugsRequest::getMethodNumber)
                .collect(Collectors.toSet());
        for (int i = 1; i <= request.getNumberOfMethods(); i++) {
            if (!methodNumbers.contains(i)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Method numbers must be sequential starting from 1");
            }
        }

        // Validate all drugIds exist
        Set<Long> drugIds = request.getMethods().stream()
                .flatMap(method -> method.getDrugs().stream())
                .map(TreatmentRegimenDrugRequest::getDrugId)
                .collect(Collectors.toSet());

        if (!drugRepository.existsByIdIn(drugIds)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "One or more drug IDs do not exist");
        }
    }



    private void saveTreatmentRegimenDrugs(List<MethodDrugsRequest> methods, TreatmentRegimen regimen) {
        methods.forEach(method -> {
            method.getDrugs().forEach(drugRequest -> {
                Drug drug = drugRepository.findById(drugRequest.getDrugId())
                        .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND,
                                "Drug not found with ID: " + drugRequest.getDrugId()));

                TreatmentRegimenDrug association = new TreatmentRegimenDrug(
                        drug,
                        method.getMethodNumber(),
                        regimen,
                        drugRequest.getNote()
                );
                treatmentRegimenDrugRepository.save(association);
            });
        });
    }

    private void addDrugsToResponse(TreatmentRegimenResponse response, Long regimenId) {
        List<TreatmentRegimenDrug> savedDrugs = treatmentRegimenDrugRepository
                .findByTreatmentRegimen_Id(regimenId);

        if (!savedDrugs.isEmpty()) {
            Map<Integer, List<Drug>> drugMap = new HashMap<>();
            savedDrugs.forEach(trd -> {
                drugMap.computeIfAbsent(trd.getMethod(), k -> new ArrayList<>())
                        .add(trd.getDrug());
            });

            List<TreatmentRegimenDrugResponse> drugMethods = drugMap.entrySet().stream()
                    .map(entry -> {
                        TreatmentRegimenDrugResponse drugMethodResponse = new TreatmentRegimenDrugResponse();
                        drugMethodResponse.setMethod(entry.getKey());
                        drugMethodResponse.setDrugs(entry.getValue().stream()
                                .map(drug -> modelMapper.map(drug, DrugResponse.class))
                                .toList());
                        return drugMethodResponse;
                    })
                    .toList();

            response.setDrugMethods(drugMethods);
        }
    }
}
