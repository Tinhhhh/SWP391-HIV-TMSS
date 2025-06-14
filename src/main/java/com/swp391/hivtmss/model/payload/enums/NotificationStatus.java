package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    UNREAD("UNREAD"),
    READ("READ"),
    HIDDEN("HIDDEN");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }
}
