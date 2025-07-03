package com.swp391.hivtmss.model.payload.enums;


import lombok.Getter;

@Getter
public enum NotificationStatusFilter {
    ALL("ALL"),
    UNREAD("UNREAD");

    private final String value;

    NotificationStatusFilter(String value) {
        this.value = value;
    }
}
