package com.swp391.hivtmss.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
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
@Table(name = "treatment_regimen_drug")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "treatmentRegimen"})
public class TreatmentRegimenDrug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_regimen_drug_id")
    private Long id;

    private int method;

    private String note;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active")
    private ActiveStatus active = ActiveStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @ManyToOne
    @JoinColumn(name = "treatment_regimen_id", nullable = false)
    private TreatmentRegimen treatmentRegimen;

    public TreatmentRegimenDrug(Drug drug, Integer method, TreatmentRegimen treatmentRegimen, String note) {
        this.drug = drug;
        this.method = method;
        this.treatmentRegimen = treatmentRegimen;
        this.note = note;
        this.active = ActiveStatus.ACTIVE;
    }
}
