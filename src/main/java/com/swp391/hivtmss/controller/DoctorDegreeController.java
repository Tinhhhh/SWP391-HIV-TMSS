package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.entity.DegreeImg;
import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.service.CloudinaryService;
import com.swp391.hivtmss.service.DoctorDegreeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor-degrees")
// @CrossOrigin(origins = "*") // Add if needed for frontend development
@RequiredArgsConstructor
public class DoctorDegreeController {

    private final DoctorDegreeService doctorDegreeService;

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> createDoctorDegree(@Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse createdDegree = doctorDegreeService.createDoctorDegree(doctorDegreeRequest);
        return ResponseBuilder.returnMessage(HttpStatus.CREATED, "Create degree successfully");
    }

    @GetMapping
    public ResponseEntity<?> getDoctorDegreeById(@RequestParam("id") Long id) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeById(id);
        return ResponseBuilder.returnData(HttpStatus.OK, "Degree retrieved successfully", doctorDegree);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getDoctorDegreeByAccountId(@PathVariable UUID accountId) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Doctor degree retrieved successfully", doctorDegree);

    }

    @GetMapping
    public ResponseEntity<?> getAllDoctorDegrees() {
        List<DoctorDegreeResponse> degrees = doctorDegreeService.getAllDoctorDegrees();
        return ResponseBuilder.returnData(HttpStatus.OK, "All degrees retrieved", degrees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctorDegree(@PathVariable Long id,
                                                                   @Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse updatedDegree = doctorDegreeService.updateDoctorDegree(id, doctorDegreeRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Update degree successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctorDegree(@PathVariable Long id) {
        doctorDegreeService.deleteDoctorDegree(id);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete degree successfully");
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDegreeImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files) {
        DoctorDegreeResponse updatedDegree = doctorDegreeService.uploadDegreeImages(id, files);
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Images uploaded successfully",
                updatedDegree
        );
    }
}