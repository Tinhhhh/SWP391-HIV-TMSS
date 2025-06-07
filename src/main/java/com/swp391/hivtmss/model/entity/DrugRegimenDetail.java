package com.swp391.hivtmss.model.entity;

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
@Table(name = "drug_regimen_detail")
@EntityListeners(AuditingEntityListener.class)
public class DrugRegimenDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drugRegimenDetailId;

    private int method;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @ManyToOne
    @JoinColumn(name = "regimen_detail_id", nullable = false)
    private RegimenDetail regimenDetail;

}
