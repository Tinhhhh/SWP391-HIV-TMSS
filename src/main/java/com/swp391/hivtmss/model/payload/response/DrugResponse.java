package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.DrugType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
