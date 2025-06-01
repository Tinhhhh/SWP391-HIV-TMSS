package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum ResultType {
    PRELIMINARY("PRELIMINARY"),
    FINAL("FINAL"),
    INDETERMINATE("INDETERMINATE");

    private final String value;

    ResultType(String value) {
        this.value = value;
    }
}
