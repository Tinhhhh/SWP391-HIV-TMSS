package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum PcrType {
    DNA("DNA"),
    RNA("RNA");

    private final String value;

    PcrType(String value) {
        this.value = value;
    }
}
