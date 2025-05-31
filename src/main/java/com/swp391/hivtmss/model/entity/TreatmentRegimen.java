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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "treatment_regimen")
@EntityListeners(AuditingEntityListener.class)
public class TreatmentRegimen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_regimen_id")
    private Long id;

    private String name;

    @Column(name = "regimen_code", unique = true, nullable = false)
    private String regimenCode;

    @Column(name = "line_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LineLevel lineLevel;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    private String note;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "treatmentRegimen")
    private List<RegimenDetail> regimenDetails;

}
