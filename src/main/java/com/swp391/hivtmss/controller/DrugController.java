package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.*;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import com.swp391.hivtmss.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drugs")
@RequiredArgsConstructor
public class DrugController {

    private final DrugService drugService;

    @Operation(summary = "Create Drug", description = "Create Drug")
    @PostMapping
    public ResponseEntity<Object> createDrug(@Valid @RequestBody DrugRequest drugRequest) {
        drugService.createDrug(drugRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your Drug created successfully");
    }


    @Operation(summary = "Get Drug By DrugID", description = "Get Drug By DrugID")
    @GetMapping
    public ResponseEntity<Object> getDrugById(@RequestParam("id") Long id) {

        return ResponseBuilder.returnData(HttpStatus.OK, "Get DrugByID Successfully",
                drugService.getDrugById(id));
    }

    @Operation(summary = "Get All Drug ", description = "Get All Drug")
    @GetMapping("/all")
    public ResponseEntity<List<DrugResponse>> getAllDrug() {
        List<DrugResponse> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(drugs);
    }

    @Operation(summary = "Delete Drug", description = "Delete Drug")
    @DeleteMapping
    public ResponseEntity<Object> deleteDrug(@RequestParam("id") Long id,
                                                 @Valid @RequestBody DrugRequest drugRequest) {

        drugService.deleteDrug(id, drugRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your Drug is Delete successfully");
    }


    @Operation(summary = "Update Drug By ID", description = "Get Drug By ID")
    @PutMapping
    public ResponseEntity<Object> updateDrug(@RequestParam("id") Long id,
                                                 @Valid @RequestBody DrugRequest drugRequest) {

        drugService.updateDrug(id, drugRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your Drug is update successfully");
    }

}


