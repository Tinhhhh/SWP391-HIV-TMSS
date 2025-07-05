package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum AppointmentChangeStatusFilter {

    ALL("ALL"),
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String status;

    AppointmentChangeStatusFilter(String status) {
        this.status = status;
    }

    public static Optional<AppointmentStatus> toAppointmentStatus(AppointmentChangeStatusFilter filter) {
        if (filter == AppointmentChangeStatusFilter.ALL) return Optional.empty();
        return Optional.of(AppointmentStatus.valueOf(filter.name()));
    }


}
