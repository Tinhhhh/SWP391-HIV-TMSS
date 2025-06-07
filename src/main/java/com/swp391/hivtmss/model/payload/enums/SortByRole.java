package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum SortByRole {
    CUSTOMER("CUSTOMER"),
    DOCTOR("DOCTOR"),
    ALL("ALL");

    private final String value;

    SortByRole(String value) {
        this.value = value;
    }
}
