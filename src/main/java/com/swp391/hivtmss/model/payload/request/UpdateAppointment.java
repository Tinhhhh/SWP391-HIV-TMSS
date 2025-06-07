package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointment {

    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("medical_history")
    private String medicalHistory;

    @JsonProperty("prognosis")
    private String prognosis;

    @JsonProperty("prevention")
    private String prevention;

    @JsonProperty("end_time")
    private Date endTime;

    private AppointmentStatus status;

    @JsonProperty("next_follow_up")
    private Date nextFollowUp;

    @JsonProperty("test_type_id")
    private Long testTypeId;
}
