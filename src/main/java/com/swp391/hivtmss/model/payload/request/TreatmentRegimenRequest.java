package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.payload.enums.LineLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TreatmentRegimenRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Applicable is required")
    private String applicable;

    @NotNull(message = "Line level is required")
    private LineLevel lineLevel;

    private String note;

    private List<TreatmentRegimenDrugRequest> treatmentRegimenDrugs;
}


