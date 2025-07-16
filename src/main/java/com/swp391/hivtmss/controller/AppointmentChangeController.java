package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatusFilter;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeType;
import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.service.AppointmentChangeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/appointment-changes")
@RequiredArgsConstructor
public class AppointmentChangeController {

    private final AppointmentChangeService appointmentChangeService;

    @Operation(summary = "Create a request to change the doctor for an appointment",
            description = "This api allows a doctor to create a request to change the doctor for a specific appointment. " +
                    "Role required: DOCTOR")
    @PostMapping
    public ResponseEntity<Object> createAppointmentChangeDoctorRequest(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("doctorId") UUID doctorId,
            @RequestParam("reason") String reason
    ) {
        appointmentChangeService.changeAppointmentSlot(appointmentId, doctorId, reason);
        return ResponseBuilder.returnMessage(HttpStatus.CREATED, "Request to change appointment doctor sent successfully");
    }

    @Operation(summary = "Update the status of an appointment change request",
            description = "This api allows updating the status of an appointment change request. " +
                    "Role required: DOCTOR")
    @PutMapping
    public ResponseEntity<Object> updateAppointmentChangeDoctorRequest(
            @RequestParam("appointmentChangeId") Long appointmentChangeId,
            @RequestParam("status") AppointmentChangeStatus status
    ) {
        appointmentChangeService.updateAppointmentChangeStatus(appointmentChangeId, status);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Appointment change request status updated successfully");
    }

    @Operation(summary = "Get all appointment change requests sent by a doctor",
            description = "This api retrieves all appointment change requests sent by a specific doctor within a date range. " +
                    "Example: 2025-06-08T08:01:00. " +
                    "Role required: DOCTOR")
    @GetMapping("/sent-requests")
    public ResponseEntity<Object> getAllAppointmentChangeRequests(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam("doctorId") UUID doctorId,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endTime
    ) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointment change requests",
                appointmentChangeService.getAllAppointmentChangeSendRequest(pageNo, pageSize, sortBy, sortDir, doctorId, startTime, endTime)
        );
    }

    @Operation(summary = "Get all appointment change requests received by a doctor",
            description = "This api retrieves all appointment change requests received by a specific doctor within a date range. " +
                    "Example: 2025-06-08T08:01:00. " +
                    "Role required: DOCTOR")
    @GetMapping("/received-requests")
    public ResponseEntity<Object> getAllAppointmentChangeReceivedRequests(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam("doctorId") UUID doctorId,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endTime
    ) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointment change requests",
                appointmentChangeService.getAllAppointmentChangeReceivedRequest(pageNo, pageSize, sortBy, sortDir, doctorId, startTime, endTime)
        );
    }


    @Operation(summary = "Get appointment change request by ID",
            description = "This api retrieves an appointment change request by its ID. " +
                    "Role required: DOCTOR")
    @GetMapping
    public ResponseEntity<Object> getAppointmentChangeById(
            @RequestParam("appointmentChangeId") Long appointmentChangeId
    ) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointment change request",
                appointmentChangeService.getAppointmentChangeById(appointmentChangeId)
        );
    }

    @Operation(summary = "Get all appointment change by a admin",
            description = "This api retrieves all appointment change by admin within a date range. " +
                    "Example: 2025-06-08T08:01:00. " +
                    "Role required: ADMIN")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllAppointmentChangeRequests(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam("status") AppointmentChangeStatusFilter status,
            @RequestParam("type") AppointmentChangeType type,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endTime
    ) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointment change requests",
                appointmentChangeService.getAllAppointmentChangeForAdmin(pageNo, pageSize, sortBy, sortDir, searchTerm, status, type, startTime, endTime)
        );
    }

    @Operation(summary = "Review appointment change request",
            description = "This api allows a doctor to review an appointment change request. " +
                    "Role required: MANAGER")
    @PutMapping("/review")
    public ResponseEntity<Object> reviewAppointmentChange(
            @RequestParam("appointmentChangeId") Long appointmentChangeId,
            @RequestParam("isApproved") boolean isApproved
    ) {
        appointmentChangeService.reviewAppointmentChange(appointmentChangeId, isApproved);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Appointment change request reviewed successfully");
    }
}
