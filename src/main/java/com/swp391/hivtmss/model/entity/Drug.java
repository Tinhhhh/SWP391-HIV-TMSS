package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import com.swp391.hivtmss.model.payload.enums.DrugType;
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
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "drug")
@EntityListeners(AuditingEntityListener.class)
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drug_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_name", nullable = false, unique = true)
    private String shortName;

    @Enumerated(EnumType.STRING)
    private DrugType type;

    @Column(name = "is_active", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "drug")
    private List<DrugRegimenDetail> drugRegimenDetails;
}
