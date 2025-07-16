package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AppointmentChangeResponse {

    @JsonProperty("appointment_change_id")
    private Long appointmentChangeId;

    private String reason;

    @JsonProperty("is_approved")
    private boolean isApproved;

    private AppointmentChangeStatus status;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty(value = "old_doctor", index = 4)
    private DoctorResponse oldDoctor;

    @JsonProperty(value = "new_doctor", index = 5)
    private DoctorResponse newDoctor;

    @JsonProperty("appointment_id")
    private Long appointmentId;
}
