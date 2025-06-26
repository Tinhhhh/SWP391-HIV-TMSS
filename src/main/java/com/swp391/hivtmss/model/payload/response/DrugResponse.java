package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.enums.DrugType;
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

    @JsonProperty(value = "drug_id", index = 1)
    private Long id;

    @JsonProperty(value = "drug_name", index = 2)
    private String name;

    @JsonProperty(value = "short_name", index = 3)
    private String shortName;

    @JsonProperty(value = "drug_type", index = 4)
    private DrugType type;

    @JsonProperty(value = "is_active", index = 5)
    private ActiveStatus isActive;

    @JsonProperty(value = "created_date", index = 6)
    private Date createdDate;
}
