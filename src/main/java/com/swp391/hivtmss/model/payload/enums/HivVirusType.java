package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum HivVirusType {
    HIV_1("HIV-1"),
    HIV_2("HIV-2"),
    DUAL_INFECTION("DUAL_INFECTION"),
    NON_REACTIVE("NON_REACTIVE");

    private final String value;

    HivVirusType(String value) {
        this.value = value;
    }
}
