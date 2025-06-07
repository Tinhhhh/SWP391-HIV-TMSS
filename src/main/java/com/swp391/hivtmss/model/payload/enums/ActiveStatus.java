package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum ActiveStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String value;

    ActiveStatus(String value) {
        this.value = value;
    }
}
