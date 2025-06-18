package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.TreatmentRegimenRequest;
import com.swp391.hivtmss.service.TreatmentRegimenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/treatment-regimens")
@RequiredArgsConstructor
public class TreatmentRegimenController {

    private final TreatmentRegimenService treatmentRegimenService;

    @Operation(summary = "get a list of treatment regimen", description = "get treatment regimen. Role required: DOCTOR, ADMIN")
    @GetMapping("/list")
    public ResponseEntity<Object> getTreatmentRegimen(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Get accounts for admin successfully",
                treatmentRegimenService.getAllTreatmentRegimenByDoctor(pageNo, pageSize, keyword, sortBy, sortDir));
    }

    @PostMapping
    @Operation(summary = "Create a new treatment regimen",
            description = "Create a new treatment regimen. Role required: ADMIN, DOCTOR")
    public ResponseEntity<Object> createTreatmentRegimen(@Valid @RequestBody TreatmentRegimenRequest request) {
        return ResponseBuilder.returnData(HttpStatus.CREATED,
                "Treatment regimen created successfully",
                treatmentRegimenService.createTreatmentRegimen(request));
    }

    @GetMapping("/detail")
    @Operation(summary = "Get treatment regimen by ID",
            description = "Get treatment regimen details by ID. Role required: ADMIN, DOCTOR")
    public ResponseEntity<Object> getTreatmentRegimen(@RequestParam Long id) {
        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment regimen retrieved successfully",
                treatmentRegimenService.getTreatmentRegimenById(id));
    }

    @PutMapping("/update")
    @Operation(summary = "Update treatment regimen",
            description = "Update an existing treatment regimen. Role required: ADMIN, DOCTOR")
    public ResponseEntity<Object> updateTreatmentRegimen(
            @RequestParam Long id,
            @Valid @RequestBody TreatmentRegimenRequest request) {
        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment regimen updated successfully",
                treatmentRegimenService.updateTreatmentRegimen(id, request));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete treatment regimen",
            description = "Delete a treatment regimen by ID. Role required: ADMIN")
    public ResponseEntity<Object> deleteTreatmentRegimen(@RequestParam Long id) {
        treatmentRegimenService.deleteTreatmentRegimen(id);
        return ResponseBuilder.returnData(HttpStatus.OK,
                "Treatment regimen deleted successfully", null);
    }

}
