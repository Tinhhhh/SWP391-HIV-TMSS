package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum AppointmentHistoryStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String status;

    AppointmentHistoryStatus(String status) {
        this.status = status;
    }

}
