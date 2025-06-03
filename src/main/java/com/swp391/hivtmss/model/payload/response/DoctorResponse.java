package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {

    @JsonProperty("doctor_id")
    private UUID id;

    @JsonProperty("full_name")
    private String fullName;

    private Gender gender;

    private String phone;

    private String avatar;

    private String introduction;

}
