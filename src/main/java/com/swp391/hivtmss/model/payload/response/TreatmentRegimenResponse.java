package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.enums.LineLevel;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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

    private LineLevel lineLevel;

    private String note;

    private ActiveStatus isActive;

    private Date createdDate;

    @JsonProperty("treatment_regimen_drugs")
    private List<TreatmentRegimenDrugResponse> drugMethods = new ArrayList<>();


}
