package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Applicable;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import com.swp391.hivtmss.model.payload.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("full_name")
    private String fullName;

    private Gender gender;

    private LocalDate dob;

    private Applicable applicable;

    @JsonProperty("chief_complaint")
    private String chiefComplaint;

    @JsonProperty("medical_history")
    private String medicalHistory;

    private String prognosis;

    private String prevention;

    @JsonProperty("is_pregnant")
    private boolean isPregnant;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_time")
    private String endTime;

    private AppointmentStatus status;

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;

    @JsonProperty("next_follow_up")
    private String nextFollowUp;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("cancel_reason")
    private String cancelReason;

    private DoctorResponse doctor;

    private CustomerResponse customer;

    @JsonProperty("diagnosis_id")
    private Long diagnosisId;

    @JsonProperty("treatment_id")
    private Long treatmentId;
}
