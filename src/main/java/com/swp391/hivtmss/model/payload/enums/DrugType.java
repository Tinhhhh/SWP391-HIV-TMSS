package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum DrugType {
    NtRTIs("NtRTIs"),
    NNRTIs("NNRTIs"),
    PIs("PIs"),
    INSTIs("INSTIs");

    private final String value;

    DrugType(String value) {
        this.value = value;
    }
}
