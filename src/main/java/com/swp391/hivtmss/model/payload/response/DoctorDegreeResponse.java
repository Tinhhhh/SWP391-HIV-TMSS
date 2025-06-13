package com.swp391.hivtmss.model.payload.response;

import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

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
    private List<String> imageUrls;

    public DoctorDegreeResponse(Long id, String name, LocalDate dob, LocalDate graduationDate,
                                Classification classification, StudyMode studyMode, LocalDate issueDate,
                                String schoolName, String regNo, UUID accountId, String accountUsername) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.graduationDate = graduationDate;
        this.classification = classification;
        this.studyMode = studyMode;
        this.issueDate = issueDate;
        this.schoolName = schoolName;
        this.regNo = regNo;
        this.accountId = accountId;
        this.accountUsername = accountUsername;
    }

}
