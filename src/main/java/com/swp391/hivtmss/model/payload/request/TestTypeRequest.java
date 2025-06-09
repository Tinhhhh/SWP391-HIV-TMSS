package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.entity.Diagnosis;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestTypeRequest {

    @JsonProperty("test_type_id")
    private Long id;

    @JsonProperty("test_type_name")
    private String name;

    @JsonProperty("test_type_description")
    private String description;

    @JsonProperty("test_type_code")
    private String code;

    private String applicable;


}
