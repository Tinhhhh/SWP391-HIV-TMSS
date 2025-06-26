package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.TreatmentRegimenDrugRequest;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentRegimenDrugResponse;
import com.swp391.hivtmss.service.TreatmentRegimenDrugService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/treatment-regimen-drugs")
@RequiredArgsConstructor
public class TreatmentRegimenDrugController {

    private final TreatmentRegimenDrugService treatmentRegimenDrugService;

    @Operation(summary = "Create a new treatment regimen drug association")
    @PostMapping
    public ResponseEntity<Object> createTreatmentRegimenDrug(
            @RequestParam("treatmentRegimenId") Long treatmentRegimenId,
            @Valid @RequestBody TreatmentRegimenDrugRequest request) {
        TreatmentRegimenDrugResponse created = treatmentRegimenDrugService.create(treatmentRegimenId, request);
        return ResponseBuilder.returnData(HttpStatus.CREATED, "Treatment Regimen Drug created successfully", created);
    }


    @Operation(summary = "Get all drugs for a treatment regimen with pagination")
    @GetMapping("/regimen")
    public ResponseEntity<Object> getAllByTreatmentRegimen(
            @RequestParam("treatmentRegimenId") Long treatmentRegimenId,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(required = false) Integer method) {

        Page<TreatmentRegimenDrugResponse> page = method != null ?
                treatmentRegimenDrugService.getAllByTreatmentRegimenAndMethod(treatmentRegimenId, method, pageNo, pageSize, sortBy, sortDir) :
                treatmentRegimenDrugService.getAllByTreatmentRegimen(treatmentRegimenId, pageNo, pageSize, sortBy, sortDir);

        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment Regimen Drugs retrieved successfully",
                new ListResponse(page.getContent(), pageNo, pageSize,
                        page.getTotalElements(), page.getTotalPages(), page.isLast()));
    }


    @Operation(summary = "Get a specific treatment regimen drug by ID")
    @GetMapping
    public ResponseEntity<Object> getTreatmentRegimenDrug(
            @RequestParam("id") Long id) {
        TreatmentRegimenDrugResponse drug = treatmentRegimenDrugService.getById(id);
        return ResponseBuilder.returnData(HttpStatus.OK, "Treatment Regimen Drug retrieved successfully", drug);
    }

    @Operation(summary = "Update a treatment regimen drug")
    @PutMapping
    public ResponseEntity<Object> updateTreatmentRegimenDrug(
            @RequestParam("id") Long id,
            @Valid @RequestBody TreatmentRegimenDrugRequest request) {
        TreatmentRegimenDrugResponse updated = treatmentRegimenDrugService.update(id, request);
        return ResponseBuilder.returnData(HttpStatus.OK, "Treatment Regimen Drug updated successfully", updated);
    }

    @Operation(summary = "Get all treatment regimens by drug with pagination")
    @GetMapping("/drug")
    public ResponseEntity<Object> getAllByDrug(
            @RequestParam("drugId") Long drugId,
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir) {

        Page<TreatmentRegimenDrugResponse> page = treatmentRegimenDrugService.getAllByDrug(drugId, pageNo, pageSize, sortBy, sortDir);

        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment Regimen Drugs retrieved successfully",
                new ListResponse(page.getContent(), pageNo, pageSize,
                        page.getTotalElements(), page.getTotalPages(), page.isLast()));
    }


    @Operation(summary = "Deactivate a treatment regimen drug")
    @DeleteMapping
    public ResponseEntity<Object> deactivateTreatmentRegimenDrug(
            @RequestParam("id") Long id) {
        TreatmentRegimenDrugResponse deactivated = treatmentRegimenDrugService.deactivate(id);
        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment Regimen Drug deactivated successfully", deactivated);
    }

    @Operation(summary = "Activate a treatment regimen drug")
    @PutMapping("/activate")
    public ResponseEntity<Object> activateTreatmentRegimenDrug(
            @RequestParam("id") Long id) {
        TreatmentRegimenDrugResponse activated = treatmentRegimenDrugService.activate(id);
        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment Regimen Drug activated successfully", activated);
    }

}



