package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
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
@Table(name = "regimen_detail")
@EntityListeners(AuditingEntityListener.class)
public class RegimenDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regimen_detail_id")
    private Long id;

    private String name;

    private String applicable;

    @Column(name = "is_active", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "treatment_regimen_id", nullable = false)
    private TreatmentRegimen treatmentRegimen;

    @OneToMany(mappedBy = "regimenDetail")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "regimenDetail")
    private List<DrugRegimenDetail> drugRegimenDetails;
}
