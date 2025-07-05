package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum AppointmentChangeType {
    ALL("ALL"),
    SENT("SENT"),
    RECEIVED("RECEIVED"),;

    private final String type;

    AppointmentChangeType(String type) {
        this.type = type;
    }

}
