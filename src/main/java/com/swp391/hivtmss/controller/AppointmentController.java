package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.request.AppointmentUpdate;
import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.service.AppointmentService;
import com.swp391.hivtmss.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static com.swp391.hivtmss.util.AppConstants.*;

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

    @Operation(summary = "Update appointment diagnosis", description = "Update the diagnosis of an appointment. Role required: DOCTOR")
    @PutMapping("/diagnosis")
    public ResponseEntity<Object> updateDiagnosis(
            @RequestBody AppointmentDiagnosisUpdate appointmentUpdate) throws MessagingException {

        appointmentService.updateAppointmentDiagnosis(appointmentUpdate);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment diagnosis updated successfully");
    }

    @Operation(summary = "Update appointment details including treatment method", description = "Update the details of an appointment. Role required: DOCTOR")
    @PutMapping("/treatment")
    public ResponseEntity<Object> updateAppointmentDetails(
            @RequestBody AppointmentUpdate appointmentUpdate) throws MessagingException {

        appointmentService.updateAppointmentTreatment(appointmentUpdate);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment treatment details updated successfully");
    }

    @Operation(summary = "Update appointment details", description = "Update the details of an appointment. Role required: CUSTOMER")
    @PutMapping("/cancel")
    public ResponseEntity<Object> cancelAppointment(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("reason") String reason) {
        appointmentService.cancelAppointment(appointmentId, reason);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Appointment cancelled successfully");
    }

    @Operation(summary = "Get appointments by customerId", description = "Retrieve a paginated list of appointments. " +
            "Example: 2025-06-08T08:01:00. " +
            "Role required: CUSTOMER")
    @GetMapping("/customer")
    public ResponseEntity<Object> getAppointmentsByCustomerId(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam("customerId") UUID customerId) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointments for customer",
                appointmentService.getAppointmentByCustomerId(pageNo, pageSize, sortBy, sortDir, customerId));
    }

    @Operation(summary = "Get appointments for doctor, admin", description = "Retrieve a paginated list of appointments. " +
            "Example: 2025-06-08T08:01:00. " +
            "Role required: DOCTOR, ADMIN")
    @GetMapping("/all")
    public ResponseEntity<Object> getAppointments(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "searchTerm", required = false) String searchTerm
    ) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved appointments",
                appointmentService.getAllAppointment(pageNo, pageSize, sortBy, sortDir, searchTerm));
    }

//    @GetMapping("test")
//    public ResponseEntity<Object> test() throws MessagingException {
//        appointmentService.testEmail();
//        return ResponseBuilder.returnMessage(HttpStatus.OK, "Test successful");
//    }


    @Operation(summary = "Get dashboard data", description = "Fetches dashboard data for admin panel within a specified date range. Example: 2025-07-05T08:01:00")
    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                               @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Dashboard data retrieved successfully", appointmentService.getDashboardByRange(startDate, endDate));
    }

    @Operation(summary = "Get monthly dashboard data", description = "Fetches monthly dashboard data for admin panel within a specified date range. Example: 2025-07-05T08:01:00 - 2025-04-22T08:01:00")
    @GetMapping("/dashboard/monthly")
    public ResponseEntity<Object> getMonthlyDashboard(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                      @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Monthly dashboard data retrieved successfully", appointmentService.getMonthlyDashboardByRange(startDate, endDate));
    }
}
