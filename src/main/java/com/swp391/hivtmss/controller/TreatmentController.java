package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Operation(summary = "Get treatment by appointment ID",
            description = "Retrieve the treatment associated with a specific appointment ID. Role required: ADMIN, DOCTOR, CUSTOMER")
    @GetMapping("/appointment")
    public ResponseEntity<Object> getTreatmentByAppointmentId(@RequestParam Long appointmentId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Successfully retrieved treatment",
                treatmentService.getTreatmentByAppointmentId(appointmentId));
    }

    @Operation(summary = "Get treatment by treatment ID",
            description = "Retrieve the treatment associated with a specific treatment ID. Role required: ADMIN, DOCTOR, CUSTOMER")
    @GetMapping()
    public ResponseEntity<Object> getTreatmentById(@RequestParam Long treatmentId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Successfully retrieved treatment ",
                treatmentService.getTreatmentById(treatmentId));
    }
}
