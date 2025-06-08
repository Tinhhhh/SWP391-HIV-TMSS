package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDiagnosisUpdate {

    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("test_type_id")
    private Long testTypeId;

    @JsonProperty("sample_type")
    private SampleType sampleType;

    private HivResult result;

    @JsonProperty("result_type")
    private ResultType resultType;

    @JsonProperty("virus_type")
    private HivVirusType virusType;

    @JsonProperty("ag_ab_result")
    private HivResult AgAbResult;

    private Long cd4;

    @JsonProperty("viral_load")
    private Long viralLoad;

    @JsonProperty("pcr_type")
    private PcrType pcrType;

    @JsonProperty("clinical_stage")
    private HivStage clinicalStage;

    private String note;

}
