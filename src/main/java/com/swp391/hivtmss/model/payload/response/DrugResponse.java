package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.enums.DrugType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrugResponse {

    @JsonProperty("drug_id")
    private Long id;

    private String name;

    @JsonProperty("short_name")
    private String shortName;

    private DrugType type;

    @JsonProperty("is_active")
    private ActiveStatus isActive;

    @JsonProperty("created_date")
    private Date createdDate;
}
