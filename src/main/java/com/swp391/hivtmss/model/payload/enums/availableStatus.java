package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum availableStatus {
    AVAILABLE("AVAILABLE"),
    UNAVAILABLE("UNAVAILABLE");

    private String value;

    availableStatus(String value) {
        this.value = value;
    }
}
