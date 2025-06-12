package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.service.TreatmentRegimenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/treatment-regimens")
@RequiredArgsConstructor
public class TreatmentRegimenController {

    private final TreatmentRegimenService treatmentRegimenService;

    @Operation(summary = "get a list of treatment regimen", description = "get treatment regimen. Role required: DOCTOR, ADMIN")
    @GetMapping
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
}
