package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum HivResult {
    REACTIVE("REACTIVE"),
    NON_REACTIVE("NON_REACTIVE"),
    INCONCLUSIVE("INCONCLUSIVE");

    private final String value;

    HivResult(String value) {
        this.value = value;
    }
}
