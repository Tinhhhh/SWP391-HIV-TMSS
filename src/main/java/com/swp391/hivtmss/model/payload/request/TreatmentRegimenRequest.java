package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.payload.enums.LineLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotNull(message = "Number of methods is required")
    @Min(value = 1, message = "At least 1 method is required")
    private Integer numberOfMethods;

    @NotEmpty(message = "Methods data is required")
    @Valid
    private List<MethodDrugsRequest> methods;

    public void validateMethodNumbers() {
        if (methods != null) {
            // Check if method numbers are sequential starting from 1
            for (int i = 0; i < methods.size(); i++) {
                if (methods.get(i).getMethodNumber() != i + 1) {
                    throw new IllegalArgumentException(
                            "Method numbers must be sequential starting from 1. Found method number "
                                    + methods.get(i).getMethodNumber() + " at position " + (i + 1));
                }
            }
        }
    }
}



