package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query(value = """
            SELECT * FROM Appointment a 
                     WHERE a.start_time - INTERVAL '1 hour' <= :startTime 
                                   AND a.start_time + INTERVAL '1 hour' >= :startTime""",
            nativeQuery = true)
    List<Appointment> findByStartTime(Date startTime);

    @Query(value = """
            SELECT * FROM Appointment a 
                     WHERE a.start_time - INTERVAL '1 hour' <= :startTime 
                                   AND a.start_time + INTERVAL '1 hour' >= :startTime
                                   AND a.doctor_id = :doctorId""",
            nativeQuery = true)
    Optional<Appointment> findByStartTimeBetweenAndDoctor_Id(Date startTime, UUID doctorId);

    List<Appointment> findByStartTimeBetweenAndDoctor_Id(Date startTime, Date endTime, UUID doctorId);

    List<Appointment> findByStatusAndStartTimeBefore(AppointmentStatus appointmentStatus, Date now);

    List<Appointment> findByStatusAndNextFollowUpBeforeAndNextFollowUpReminderFalse(AppointmentStatus appointmentStatus, Date now);

}
