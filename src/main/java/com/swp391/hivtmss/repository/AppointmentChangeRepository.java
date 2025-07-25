package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.AppointmentChange;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentChangeRepository extends JpaRepository<AppointmentChange, Long>, JpaSpecificationExecutor<AppointmentChange> {

    List<AppointmentChange> findByAppointment_Id(Long id);

    List<AppointmentChange> findByStatusAndCreatedDateBefore(AppointmentChangeStatus appointmentChangeStatus, Date now);

    Page<AppointmentChange> findByOldDoctor_IdAndCreatedDateBetween(UUID doctorId, Date startTime, Date endTime, Pageable pageable);

    Page<AppointmentChange> findByNewDoctor_IdAndCreatedDateBetween(UUID doctorId, Date startTime, Date endTime, Pageable pageable);

    Optional<AppointmentChange> findByAppointment_IdAndStatus(Long appointmentId, AppointmentChangeStatus appointmentChangeStatus);
}
