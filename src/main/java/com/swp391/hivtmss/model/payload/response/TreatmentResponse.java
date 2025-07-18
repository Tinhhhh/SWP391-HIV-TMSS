package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponse {

    @JsonProperty("treatment_id")
    private Long treatmentId;

    @JsonProperty("treatment_regimen_id")
    private Long treatmentRegimenId;

    private String name;

    private int method;

    private List<DrugResponse> drugs;

}
