package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    CUSTOMER("CUSTOMER"),
    DOCTOR("DOCTOR"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN");

    private final String role;

    RoleName(String role) {
        this.role = role;
    }
}
