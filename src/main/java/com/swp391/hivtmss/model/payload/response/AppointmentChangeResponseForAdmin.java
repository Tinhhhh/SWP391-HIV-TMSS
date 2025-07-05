package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentChangeResponseForAdmin {

    @JsonProperty("appointment_change_type")
    private AppointmentChangeType type;

    @JsonProperty("appointment_change_responses")
    private ListResponse response;
}
