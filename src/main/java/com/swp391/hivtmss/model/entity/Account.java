package com.swp391.hivtmss.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swp391.hivtmss.model.payload.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account", indexes = {
        @Index(name = "idx_email", columnList = "email"),
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "introduction")
    private String introduction;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "account")
    private DoctorDegree doctorDegree;

    @OneToMany(mappedBy = "account")
    private List<Blog> blogs;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentsAsDoctor;

    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointmentsAsCustomer;

    @OneToMany(mappedBy = "oldDoctor")
    private List<AppointmentChange> appointmentChangesAsOldDoctor;

    @OneToMany(mappedBy = "newDoctor")
    private List<AppointmentChange> appointmentChangesAsNewDoctor;

    @OneToMany(mappedBy = "account")
    private List<Notification> notifications;

    @JsonIgnore
    public String fullName() {
        return lastName + " " + firstName;
    }
}
