package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {

    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }
}
