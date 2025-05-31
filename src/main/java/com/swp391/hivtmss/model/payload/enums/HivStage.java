package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum HivStage {
    STAGE_I("STAGE_I"),
    STAGE_II("STAGE_II"),
    STAGE_III("STAGE_III"),
    STAGE_IV("STAGE_IV");

    private final String value;

    HivStage(String value) {
        this.value = value;
    }
}
