package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    CREATE_ACCOUNT("CREATE_ACCOUNT"),
    FORGOT_PASSWORD("FORGOT_PASSWORD");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
