package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.enums.LineLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treatment_regimen")
@EntityListeners(AuditingEntityListener.class)
public class TreatmentRegimen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_regimen_id")
    private Long id;

    private String name;

    private String applicable;

    @Enumerated(EnumType.STRING)
    private LineLevel lineLevel;

    private String note;

    @Column(name = "is_active", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "treatmentRegimen")
    private List<Treatment> treatments;

    @OneToMany(mappedBy = "treatmentRegimen")
    private List<TreatmentRegimenDrug> treatmentRegimenDrugs;
}
