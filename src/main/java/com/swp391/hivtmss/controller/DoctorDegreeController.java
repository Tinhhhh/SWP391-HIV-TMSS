package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.service.CloudinaryService;
import com.swp391.hivtmss.service.DoctorDegreeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctor-degrees")
@RequiredArgsConstructor
public class DoctorDegreeController {

    private final DoctorDegreeService doctorDegreeService;
    private final CloudinaryService cloudinaryService;

    @PostMapping
    @Operation(summary = "Create a new doctor degree",
            description = "Create a new doctor degree with the provided details. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> createDoctorDegree(@Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse createdDegree = doctorDegreeService.createDoctorDegree(doctorDegreeRequest);
        return ResponseBuilder.returnData(HttpStatus.CREATED, "Create degree successfully", createdDegree);
    }

    @GetMapping("/by-id")
    @Operation(summary = "Get doctor degree by ID",
            description = "Retrieve a doctor degree by its ID. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> getDoctorDegreeById(@RequestParam("id") Long id) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeById(id);
        return ResponseBuilder.returnData(HttpStatus.OK, "Degree retrieved successfully", doctorDegree);
    }

    @GetMapping("/account")
    @Operation(summary = "Get doctor degree by account ID",
            description = "Retrieve a doctor degree by account ID. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> getDoctorDegreeByAccountId(@RequestParam("accountId") UUID accountId) {
        DoctorDegreeResponse doctorDegree = doctorDegreeService.getDoctorDegreeByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Doctor degree retrieved successfully", doctorDegree);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all doctor degrees",
            description = "Retrieve all doctor degrees. Role required: ADMIN")
    public ResponseEntity<?> getAllDoctorDegrees() {
        List<DoctorDegreeResponse> degrees = doctorDegreeService.getAllDoctorDegrees();
        return ResponseBuilder.returnData(HttpStatus.OK, "All degrees retrieved successfully", degrees);
    }

    @PutMapping("/update")
    @Operation(summary = "Update doctor degree",
            description = "Update an existing doctor degree. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> updateDoctorDegree(
            @RequestParam("id") Long id,
            @Valid @RequestBody DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegreeResponse updatedDegree = doctorDegreeService.updateDoctorDegree(id, doctorDegreeRequest);
        return ResponseBuilder.returnData(HttpStatus.OK, "Update degree successfully", updatedDegree);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete doctor degree",
            description = "Delete a doctor degree by ID. Role required: ADMIN")
    public ResponseEntity<?> deleteDoctorDegree(@RequestParam("id") Long id) {
        doctorDegreeService.deleteDoctorDegree(id);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete degree successfully");
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload doctor degree image",
            description = "Upload or replace a doctor degree image. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> uploadDegreeImage(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {
        DoctorDegreeResponse updatedDegree = doctorDegreeService.uploadDegreeImages(id, List.of(file));
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Image uploaded successfully",
                updatedDegree
        );
    }

    @DeleteMapping("/images")
    @Operation(summary = "Delete all images of a doctor degree",
            description = "Delete all images associated with a doctor degree. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> deleteAllImages(@RequestParam("id") Long id) {
        DoctorDegreeResponse response = doctorDegreeService.deleteAllImages(id);
        return ResponseBuilder.returnData(HttpStatus.OK, "All images deleted successfully", response);
    }

    @DeleteMapping("/images/by-url")
    @Operation(summary = "Delete specific image of a doctor degree",
            description = "Delete a specific image by URL from a doctor degree. Role required: ADMIN, DOCTOR")
    public ResponseEntity<?> deleteImageByUrl(
            @RequestParam("id") Long id,
            @RequestParam("imageUrl") String imageUrl) {
        DoctorDegreeResponse response = doctorDegreeService.deleteImageByUrl(id, imageUrl);
        return ResponseBuilder.returnData(HttpStatus.OK, "Image deleted successfully", response);
    }
}
