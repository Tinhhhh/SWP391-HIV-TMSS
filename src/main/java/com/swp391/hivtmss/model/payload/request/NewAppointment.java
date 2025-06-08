package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Applicable;
import com.swp391.hivtmss.model.payload.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAppointment {

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("doctor_id")
    private UUID doctorId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private Gender gender;

    private LocalDate dob;

    private Applicable applicable;

    @FutureOrPresent
    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("chief_complaint")
    private String chiefComplaint;

    @JsonProperty("is_pregnant")
    private boolean isPregnant;

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;
}
