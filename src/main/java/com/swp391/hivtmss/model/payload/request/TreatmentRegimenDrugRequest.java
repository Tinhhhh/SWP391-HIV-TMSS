package com.swp391.hivtmss.model.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRegimenDrugRequest {
    @NotNull(message = "Drug ID is required")
    private Long drugId;
    @NotNull(message = "Method is required")
    private int method;

    private String note;
}