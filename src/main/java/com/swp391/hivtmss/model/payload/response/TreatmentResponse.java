package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponse {

    @JsonProperty("treatment_id")
    private Long treatmentId;

    @JsonProperty("regiment_detail_id")
    private Long regimentDetailId;

    @JsonProperty("regiment_name")
    private String regimentName;

    private int method;



}
