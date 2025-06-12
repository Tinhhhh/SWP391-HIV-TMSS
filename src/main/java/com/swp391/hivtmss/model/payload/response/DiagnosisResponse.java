package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.TestType;
import com.swp391.hivtmss.model.payload.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisResponse {

    @JsonProperty("diagnosis_id")
    private Long id;

    @JsonProperty("sample_type")
    private SampleType sampleType;

    private HivResult result;

    @JsonProperty("result_type")
    private ResultType resultType;

    @JsonProperty("virus_type")
    private HivVirusType virusType;

    @JsonProperty("ag_ab_result")
    private HivResult agAbResult;

    private Long cd4;

    @JsonProperty("viral_load")
    private Long viralLoad;

    @JsonProperty("pcr_type")
    private PcrType pcrType;

    @JsonProperty("clinical_stage")
    private HivStage clinicalStage;

    private String createdDate;

    private TestTypeResponse testType;
}
