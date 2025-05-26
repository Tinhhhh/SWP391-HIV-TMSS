package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum StudyMode {
    FULL_TIME("FULL_TIME"),
    PART_TIME("PART_TIME");

    private final String value;

    StudyMode(String value) {
        this.value = value;
    }
}
