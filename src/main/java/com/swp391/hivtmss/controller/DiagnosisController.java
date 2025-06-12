package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "Get diagnosis by appointment ID",
            description = "Retrieve the diagnosis associated with a specific appointment ID. Role required: ADMIN, DOCTOR, CUSTOMER")
    @GetMapping("/appointment")
    public ResponseEntity<Object> getDiagnosisByAppointmentId(@RequestParam Long appointmentId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Successfully retrieved diagnosis for appointment",
                diagnosisService.getDiagnosisByAppointmentId(appointmentId));
    }

    @Operation(summary = "Get diagnosis by diagnosis ID",
            description = "Retrieve the diagnosis associated with a specific diagnosis ID. Role required: ADMIN, DOCTOR, CUSTOMER")
    @GetMapping()
    public ResponseEntity<Object> getDiagnosis(@RequestParam Long diagnosisId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Successfully retrieved diagnosis ",
                diagnosisService.getDiagnosisByAppointmentId(diagnosisId));
    }

    @Operation(summary = "Edit diagnosis for an appointment",
            description = "Update the diagnosis details for a specific appointment. Role required: ADMIN, DOCTOR")
    @PutMapping("/appointment")
    public ResponseEntity<Object> editDiagnosis(@RequestParam Long appointmentId, @RequestBody AppointmentDiagnosisUpdate update) {
        diagnosisService.editDiagnosis(appointmentId, update);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK,
                "Diagnosis updated successfully");
    }




}
