package com.swp391.hivtmss.model.entity;

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
@Table(name = "treatment")
@EntityListeners(AuditingEntityListener.class)
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long id;

    private int method;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "treatment_regimen_id", nullable = false)
    private TreatmentRegimen treatmentRegimen;

    @OneToMany(mappedBy = "treatment")
    private List<Appointment> appointments;

}
