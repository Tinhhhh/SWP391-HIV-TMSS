package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.request.AppointmentUpdate;
import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.service.AppointmentService;
import com.swp391.hivtmss.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointment", description = "APIs for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "get a list of available doctor in a specific time for making an appointment", description = "time, format: yyyy-MM-ddTHH:mm:ss. Example: 2025-06-08T08:01:00. Role required: CUSTOMER")
    @GetMapping("/available-doctors")
    public ResponseEntity<Object> getAvailableDoctors(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved data",
                appointmentService.getAvailableDoctors(DateUtil.convertToDate(startTime)));
    }

    @Operation(summary = "Create a new appointment", description = "Create a new appointment with the provided details. Role required: CUSTOMER")
    @PostMapping
    public ResponseEntity<Object> createAppointment(
//            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestBody NewAppointment newAppointment) {
        appointmentService.createAppointment(newAppointment);
        return ResponseBuilder.returnMessage(
                HttpStatus.CREATED, "Appointment created successfully");
    }

    @Operation(summary = "Get appointments by date range", description = "Get appointments within a specific date range. Example: 2025-06-08T08:01:00, Role required: ADMIN, DOCTOR")
    @GetMapping("/by-range")
    public ResponseEntity<Object> getAppointmentsByRange(
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endTime,
            @RequestParam("doctorId") UUID doctorId) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Successfully retrieved appointments",
                appointmentService.getAppointmentByRange(startTime, endTime, doctorId));
    }

    @Operation(summary = "Get appointment by ID", description = "Retrieve an appointment by its ID. Role required: ADMIN, DOCTOR, CUSTOMER")
    @GetMapping
    public ResponseEntity<Object> getAppointmentsById(
            @RequestParam("appointmentId") Long appointmentId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointment",
                appointmentService.getAppointmentById(appointmentId));
    }

    @Operation(summary = "Update appointment diagnosis", description = "Update the diagnosis of an appointment. Role required: MANAGER")
    @PutMapping("/diagnosis")
    public ResponseEntity<Object> updateDiagnosis(
            @RequestBody AppointmentDiagnosisUpdate appointmentUpdate) {

        appointmentService.updateAppointmentDiagnosis(appointmentUpdate);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment diagnosis updated successfully");
    }

    @Operation(summary = "Update appointment details including treatment method", description = "Update the details of an appointment. Role required: DOCTOR")
    @PutMapping("/treatment")
    public ResponseEntity<Object> updateAppointmentDetails(
            @RequestBody AppointmentUpdate appointmentUpdate) {

        appointmentService.updateAppointmentTreatment(appointmentUpdate);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment treatment details updated successfully");
    }

    @Operation(summary = "Update appointment details", description = "Update the details of an appointment. Role required: DOCTOR")
    @PutMapping("/cancel")
    public ResponseEntity<Object> cancelAppointment(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("reason") String reason) {
        appointmentService.cancelAppointment(appointmentId, reason);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment cancelled successfully");
    }


}
