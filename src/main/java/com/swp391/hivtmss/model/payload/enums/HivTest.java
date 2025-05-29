package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum HivTest {
    SCREENING("SCREENING"),
    CONFIRMATORY_3TEST("CONFIRMATORY_3TEST"),
    PCR("PCR"),
    WB_TEST("WB_TEST"),
    VIRALLOAD_TEST("VIRALLOAD_TEST"),
    CD4_TEST("CD4_TEST");

    //Screening: là test sơ bộ để phát hiện HIV.
    //Confirmatory 3 test: là test khẳng định HIV bằng 3 phương pháp khác nhau.
    //PCR: là test phát hiện HIV cho trẻ sơ sinh hoặc người lớn.
    private final String value;

    HivTest(String value) {
        this.value = value;
    }

}
