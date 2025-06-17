package com.swp391.hivtmss.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp391.hivtmss.model.payload.enums.Applicable;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import com.swp391.hivtmss.model.payload.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
@EntityListeners(AuditingEntityListener.class)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Applicable applicable;

    @Column(name = "chief_complaint", nullable = false)
    private String chiefComplaint;

    @Column(name = "medical_history")
    private String medicalHistory;

    private String prognosis;

    private String prevention;

    @Column(name = "is_pregnant", nullable = false)
    private boolean isPregnant;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;

    @Column(name = "next_follow_up")
    private Date nextFollowUp;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Account doctor;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;

    @OneToOne
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentChange> appointmentHistories;

    @JsonIgnore
    public String fullName() {
        return lastName + " " + firstName;
    }

}
