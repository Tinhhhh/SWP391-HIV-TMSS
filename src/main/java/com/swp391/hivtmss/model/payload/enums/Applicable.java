package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum Applicable {
    Adults("Adults"),
    PregnantWomen("Pregnant"),
    Adolescents("Adolescents"),
    Infant("Infant");

    private final String value;

    Applicable(String value) {
        this.value = value;
    }

}
