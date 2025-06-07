package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.model.payload.request.UpdateAppointment;
import com.swp391.hivtmss.model.payload.response.DoctorResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    List<DoctorResponse> getAvailableDoctors(Date startTime);
    void createAppointment(NewAppointment newAppointment);

    void updateAppointment(UpdateAppointment updateAppointment);
}
