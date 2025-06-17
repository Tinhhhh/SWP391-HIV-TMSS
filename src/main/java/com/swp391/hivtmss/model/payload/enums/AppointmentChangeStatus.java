package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum AppointmentChangeStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String status;

    AppointmentChangeStatus(String status) {
        this.status = status;
    }

}
