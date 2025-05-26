package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum BlogStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String status;

    BlogStatus(String status) {
        this.status = status;
    }
}
