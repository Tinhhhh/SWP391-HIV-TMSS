package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum SampleType {
    WHOLE_BLOOD("WHOLE_BLOOD"),
    PLASMA("PLASMA"),
    SERUM("SERUM");

    private final String value;

    SampleType(String value) {
        this.value = value;
    }
}
