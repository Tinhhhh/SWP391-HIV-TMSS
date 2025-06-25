package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Drug;
import com.swp391.hivtmss.model.entity.TreatmentRegimen;
import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.TreatmentRegimenDrugRequest;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenDrugResponse;
import com.swp391.hivtmss.repository.DrugRepository;
import com.swp391.hivtmss.repository.TreatmentRegimenRepository;
import com.swp391.hivtmss.repository.TreatmentRegimenDrugRepository;
import com.swp391.hivtmss.service.TreatmentRegimenDrugService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentRegimenDrugServiceImpl implements TreatmentRegimenDrugService {

    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;
    private final TreatmentRegimenRepository treatmentRegimenRepository;
    private final DrugRepository drugRepository;
    private final ModelMapper modelMapper;

    private TreatmentRegimenDrugResponse mapToDTO(TreatmentRegimenDrug drug) {
        TreatmentRegimenDrugResponse response = new TreatmentRegimenDrugResponse();
        response.setId(drug.getId());
        response.setMethod(drug.getMethod());
        response.setNote(drug.getNote());
        response.setCreatedDate(drug.getCreatedDate());
        response.setActive(drug.getActive());
        DrugResponse drugResponse = modelMapper.map(drug.getDrug(), DrugResponse.class);
        response.setDrugs(Collections.singletonList(drugResponse));
        return response;
    }


    private TreatmentRegimenDrug getEntityById(Long id) {
        return treatmentRegimenDrugRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.NOT_FOUND,
                        "Treatment Regimen Drug not found with id: " + id));
    }

    @Override
    @Transactional
    public TreatmentRegimenDrugResponse create(Long treatmentRegimenId, TreatmentRegimenDrugRequest request) {
        if (treatmentRegimenDrugRepository.existsByDrugIdAndTreatmentRegimenIdAndMethod(
                request.getDrugId(), treatmentRegimenId, request.getMethod())) {
            throw new ResourceNotFoundException("This drug is already associated with the treatment regimen for this method");
        }

        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(treatmentRegimenId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment Regimen not found"));

        Drug drug = drugRepository.findById(request.getDrugId())
                .orElseThrow(() -> new ResourceNotFoundException("Drug not found"));

        TreatmentRegimenDrug treatmentRegimenDrug = new TreatmentRegimenDrug();
        treatmentRegimenDrug.setTreatmentRegimen(treatmentRegimen);
        treatmentRegimenDrug.setDrug(drug);
        treatmentRegimenDrug.setMethod(request.getMethod());
        treatmentRegimenDrug.setNote(request.getNote());
        treatmentRegimenDrug.setActive(ActiveStatus.ACTIVE);
        return mapToDTO(treatmentRegimenDrugRepository.save(treatmentRegimenDrug));
    }




    @Override
    public TreatmentRegimenDrugResponse getById(Long id) {
        return mapToDTO(getEntityById(id));
    }


    @Override
    @Transactional
    public TreatmentRegimenDrugResponse update(Long id, TreatmentRegimenDrugRequest request) {
        TreatmentRegimenDrug treatmentRegimenDrug = getEntityById(id);

        Drug drug = drugRepository.findById(request.getDrugId())
                .orElseThrow(() -> new ResourceNotFoundException("Drug not found"));

        treatmentRegimenDrug.setDrug(drug);
        treatmentRegimenDrug.setMethod(request.getMethod());
        treatmentRegimenDrug.setNote(request.getNote());

        TreatmentRegimenDrug updated = treatmentRegimenDrugRepository.save(treatmentRegimenDrug);
        return mapToDTO(updated);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!treatmentRegimenDrugRepository.existsById(id)) {
            throw new ResourceNotFoundException("Treatment Regimen Drug not found");
        }
        treatmentRegimenDrugRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteAllByTreatmentRegimen(Long treatmentRegimenId) {
        treatmentRegimenDrugRepository.deleteByTreatmentRegimenId(treatmentRegimenId);
    }

    @Override
    public Page<TreatmentRegimenDrugResponse> getAllByTreatmentRegimen(Long treatmentRegimenId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<TreatmentRegimenDrug> page = treatmentRegimenDrugRepository.findByTreatmentRegimen_Id(treatmentRegimenId, pageable);
        return page.map(this::mapToDTO);
    }

    @Override
    public Page<TreatmentRegimenDrugResponse> getAllByTreatmentRegimenAndMethod(Long treatmentRegimenId, int method, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<TreatmentRegimenDrug> page = treatmentRegimenDrugRepository.findByTreatmentRegimen_IdAndMethod(treatmentRegimenId, method, pageable);
        return page.map(this::mapToDTO);
    }

    @Override
    public Page<TreatmentRegimenDrugResponse> getAllByDrug(Long drugId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<TreatmentRegimenDrug> page = treatmentRegimenDrugRepository.findByDrug_Id(drugId, pageable);
        return page.map(this::mapToDTO);
    }

    @Override
    @Transactional
    public TreatmentRegimenDrugResponse deactivate(Long id) {
        TreatmentRegimenDrug entity = getEntityById(id);
        entity.setActive(ActiveStatus.INACTIVE);
        return mapToDTO(treatmentRegimenDrugRepository.save(entity));
    }

    @Override
    @Transactional
    public TreatmentRegimenDrugResponse activate(Long id) {
        TreatmentRegimenDrug entity = getEntityById(id);
        entity.setActive(ActiveStatus.ACTIVE);
        return mapToDTO(treatmentRegimenDrugRepository.save(entity));
    }




}
