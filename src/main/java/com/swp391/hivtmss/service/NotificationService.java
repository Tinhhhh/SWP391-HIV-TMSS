package com.swp391.hivtmss.service;

import java.util.UUID;

public interface NotificationService {

    //When new appointment is created
    void newAppointment(UUID doctorId, UUID customerId, Long appointmentId);

    void appointmentFinished(UUID customerId);

    void appointmentCancelled(UUID doctorId, String reason);

    void appointmentDoctorChange(UUID oldDoctorId, UUID customerId, UUID newDoctorId);

}
