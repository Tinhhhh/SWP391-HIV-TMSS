package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum HivResult {
    POSITIVE("POSITIVE"),
    NEGATIVE("NEGATIVE"),
    INCONCLUSIVE("INCONCLUSIVE");

    private final String value;

    HivResult(String value) {
        this.value = value;
    }
}
