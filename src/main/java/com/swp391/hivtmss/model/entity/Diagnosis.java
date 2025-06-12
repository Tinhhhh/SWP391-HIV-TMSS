package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diagnosis")
@EntityListeners(AuditingEntityListener.class)
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_id")
    private Long id;

    @Column(name = "sample_type")
    @Enumerated(EnumType.STRING)
    private SampleType sampleType;

    @Enumerated(EnumType.STRING)
    private HivResult result;

    @Column(name = "result_type")
    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    @Column(name = "virus_type")
    @Enumerated(EnumType.STRING)
    private HivVirusType virusType;

    @Column(name = "ag_ab_result")
    @Enumerated(EnumType.STRING)
    private HivResult agAbResult;

    @Column(name = "cd4")
    private Long cd4;

    @Column(name = "viral_load")
    private Long viralLoad;

    @Column(name = "pcr_type")
    @Enumerated(EnumType.STRING)
    private PcrType pcrType;

    @Column(name = "clinical_stage")
    @Enumerated(EnumType.STRING)
    private HivStage clinicalStage;

    private String note;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToOne(mappedBy = "diagnosis")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "test_type_id", nullable = false)
    private TestType testType;
}
