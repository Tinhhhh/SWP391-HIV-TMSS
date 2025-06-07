package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointment", description = "APIs for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "get a list of available doctor in a specific time for making an appointment", description = "time, format: yyyy-MM-ddTHH:mm:ss. Example: 2025-04-02T08:01:00")
    @GetMapping("/available-doctors")
    public ResponseEntity<Object> getAvailableDoctors(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved data",
                appointmentService.getAvailableDoctors(DateUtil.convertToDate(startTime)));
    }

    @Operation(summary = "Create a new appointment", description = "Create a new appointment with the provided details")
    @PostMapping
    public ResponseEntity<Object> createAppointment(
//            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestBody NewAppointment newAppointment) {
        appointmentService.createAppointment(newAppointment);
        return ResponseBuilder.returnMessage(
                HttpStatus.CREATED, "Appointment created successfully");
    }

}
