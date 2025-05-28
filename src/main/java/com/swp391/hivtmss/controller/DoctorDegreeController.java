package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.service.DoctorDegreeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor-degrees")
// @CrossOrigin(origins = "*") // Add if needed for frontend development
public class DoctorDegreeController {

    @Autowired
    private DoctorDegreeService doctorDegreeService;

    @PostMapping
    public ResponseEntity<DoctorDegreeResponse> createDoctorDegree(@Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse createdDegree = doctorDegreeService.createDoctorDegree(doctorDegreeRequest);
        return new ResponseEntity<>(createdDegree, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDegreeResponse> getDoctorDegreeById(@PathVariable Long id) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeById(id);
        return ResponseEntity.ok(doctorDegree);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<DoctorDegreeResponse> getDoctorDegreeByAccountId(@PathVariable UUID accountId) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeByAccountId(accountId);
        return ResponseEntity.ok(doctorDegree);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDegreeResponse>> getAllDoctorDegrees() {
        List<DoctorDegreeResponse> degrees = doctorDegreeService.getAllDoctorDegrees();
        return ResponseEntity.ok(degrees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDegreeResponse> updateDoctorDegree(@PathVariable Long id,
                                                                   @Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse updatedDegree = doctorDegreeService.updateDoctorDegree(id, doctorDegreeRequest);
        return ResponseEntity.ok(updatedDegree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorDegree(@PathVariable Long id) {
        doctorDegreeService.deleteDoctorDegree(id);
        return ResponseEntity.noContent().build();
    }
}