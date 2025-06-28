package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentRegimenDrugResponse {

    @Schema(description = "Treatment regimen drug ID")
    @JsonProperty("id")
    private Long id;

    private int method;

    private List<DrugResponse> drugs;

    @Schema(description = "Additional notes")
    private String note;

    @Schema(description = "Active status")
    @JsonProperty("is_active")
    private ActiveStatus active;

    @Schema(description = "Creation date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_date")
    private Date createdDate;
}
