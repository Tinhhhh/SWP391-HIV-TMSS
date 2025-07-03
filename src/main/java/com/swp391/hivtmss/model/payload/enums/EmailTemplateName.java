package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    CREATE_ACCOUNT("CREATE_ACCOUNT"),
    FORGOT_PASSWORD("FORGOT_PASSWORD"),
    APPOINTMENT_REMINDER("APPOINTMENT_REMINDER"),
    APPOINTMENT_FINISHED("APPOINTMENT_FINISHED");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
