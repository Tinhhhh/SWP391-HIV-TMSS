package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum Classification {
    EXCELLENT("EXCELLENT"),
    VERY_GOOD("VERY_GOOD"),
    GOOD("GOOD"),
    AVERAGE("AVERAGE");

    private final String value;

    Classification(String value) {
        this.value = value;
    }

}
