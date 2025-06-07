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
@Table(name = "regimen_detail")
@EntityListeners(AuditingEntityListener.class)
public class RegimenDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regimen_detail_id")
    private Long id;

    private String name;

    private String applicable;

    private String note;

    @Column(name = "line_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LineLevel lineLevel;

    @Column(name = "is_active", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "regimenDetail")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "regimenDetail")
    private List<DrugRegimenDetail> drugRegimenDetails;
}
