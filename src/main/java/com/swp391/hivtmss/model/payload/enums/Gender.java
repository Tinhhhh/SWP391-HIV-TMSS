package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
