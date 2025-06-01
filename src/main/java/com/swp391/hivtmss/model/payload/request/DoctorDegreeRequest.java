package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DoctorDegreeRequest {

    @NotBlank(message = "Doctor's name cannot be blank")
    private String name;

    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dob;

    @NotNull(message = "Graduation date cannot be null")
    private LocalDate graduationDate;

    @NotNull(message = "Classification cannot be null")
    private Classification classification;

    @NotNull(message = "Study mode cannot be null")
    private StudyMode studyMode;

    @NotNull(message = "Issue date cannot be null")
    private LocalDate issueDate;

    @NotBlank(message = "School name cannot be blank")
    private String schoolName;

    @NotBlank(message = "Registration number cannot be blank")
    private String regNo;

    @NotNull(message = "Account ID cannot be null")
    private UUID accountId;
}
