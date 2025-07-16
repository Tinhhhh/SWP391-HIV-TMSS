package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
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
@Table(name = "appointment_change")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_change_id")
    private Long id;

    private String reason;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Enumerated(EnumType.STRING)
    private AppointmentChangeStatus status;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "old_doctor_id", nullable = false)
    private Account oldDoctor;

    @ManyToOne
    @JoinColumn(name = "new_doctor_id", nullable = false)
    private Account newDoctor;

}
