package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Applicable;
import com.swp391.hivtmss.model.payload.enums.Gender;
import jakarta.validation.constraints.FutureOrPresent;
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
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private Gender gender;

    private LocalDate dob;

    private Applicable applicable;

    @JsonProperty("medical_history")
    private String medicalHistory;

    private String prognosis;

    private String prevention;

    private boolean isPregnant;

    @FutureOrPresent
    @JsonProperty("end_time")
    private Date endTime;

    @FutureOrPresent
    @JsonProperty("next_follow_up")
    private Date nextFollowUp;

    private int method;

    @JsonProperty("regiment_detail_id")
    private Long regimentDetailId;

}
