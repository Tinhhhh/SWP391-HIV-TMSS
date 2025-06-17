package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    @Override
    public void newAppointment(UUID doctorId, UUID customerId) {

    }

    @Override
    public void appointmentFinished(UUID customerId) {

    }

    @Override
    public void appointmentCancelled(UUID doctorId, String reason) {

    }

    @Override
    public void appointmentDoctorChange(UUID oldDoctorId, UUID customerId, UUID newDoctorId) {

    }
}
