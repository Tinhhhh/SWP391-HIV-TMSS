package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DoctorDegree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_degree_id")
    private Long id;

    private String name;

    private LocalDate dob;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Enumerated(EnumType.STRING)
    private Classification classification;

    @Column(name = "study_mode")
    @Enumerated(EnumType.STRING)
    private StudyMode studyMode;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "reg_no")
    private String regNo;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "doctorDegree", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DegreeImg> degreeImgs = new ArrayList<>();

}
