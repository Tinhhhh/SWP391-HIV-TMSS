package com.swp391.hivtmss.model.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MethodDrugsRequest {

    @NotNull(message = "Method number is required")
    @Min(value = 1, message = "Method number must be ≥ 1")
    private int methodNumber;

    @NotEmpty(message = "Drugs list cannot be empty")
    @Size(min = 3, max = 3, message = "Each method must have exactly 3 drugs")
    @Valid
    private  List<TreatmentRegimenDrugRequest> drugs;
}
