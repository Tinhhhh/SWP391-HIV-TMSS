package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TreatmentRegimenResponse {

    @JsonProperty("treatment_regimen_id")
    private Long id;

    private String name;

    private String applicable;

    @JsonProperty("treatment_regimen_drugs")
    private List<TreatmentRegimenDrugResponse> drugMethods;


}
