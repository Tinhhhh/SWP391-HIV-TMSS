package com.swp391.hivtmss.model.payload.response;

import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDegreeResponse {
    private Long id;
    private String name;
    private LocalDate dob;
    private LocalDate graduationDate;
    private Classification classification;
    private StudyMode studyMode;
    private LocalDate issueDate;
    private String schoolName;
    private String regNo;
    private UUID accountId;
    private String accountUsername; //show account basic info
}
