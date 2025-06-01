package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum LineLevel {
    FIRST_LINE("FIRST_LINE"),
    SECOND_LINE("SECOND_LINE"),
    THIRD_LINE("THIRD_LINE"),
    PEP("PEP"),
    PREP("PREP");

    private final String value;

    LineLevel(String value) {
        this.value = value;
    }

}
