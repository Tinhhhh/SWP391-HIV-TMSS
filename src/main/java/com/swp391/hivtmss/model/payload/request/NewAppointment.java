package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @FutureOrPresent
    @JsonProperty("start_time")
    private Date StartTime;

    @JsonProperty("chief_complaint")
    private String chiefComplaint;

    @JsonProperty("is_pregnant")
    private boolean isPregnant;

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;
}
