package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DoctorDegree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate dob;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    private Classification classification;

    @Column(name = "study_mode")
    private StudyMode studyMode;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "reg_no")
    private String regNo;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "doctorDegree")
    private List<DegreeImg> degreeImgs;

}
