package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Applicable;
import com.swp391.hivtmss.model.payload.enums.Gender;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdate {

    private Long appointmentId;

    @JsonProperty("first_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "First name contain only characters")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @JsonProperty("last_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Last name contain only characters")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    private Gender gender;

    private LocalDate dob;

    private Applicable applicable;

    @JsonProperty("medical_history")
    @Size(max = 200, message = "medical history must be less than 200 characters")
    private String medicalHistory;

    @Size(max = 200, message = "prognosis must be less than 50 characters")
    private String prognosis;

    @Size(max = 200, message = "prevention must be less than 50 characters")
    private String prevention;

    private boolean isPregnant;

    @FutureOrPresent
    @JsonProperty("end_time")
    private Date endTime;

    @FutureOrPresent
    @JsonProperty("next_follow_up")
    private Date nextFollowUp;

    private int method;

    @JsonProperty("treatment_regimen_id")
    private Long treatmentRegimenId;

}
