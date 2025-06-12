package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.request.AppointmentUpdate;
import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.model.payload.request.UpdateAppointment;
import com.swp391.hivtmss.model.payload.response.AppointmentResponse;
import com.swp391.hivtmss.model.payload.response.DoctorResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    List<DoctorResponse> getAvailableDoctors(Date startTime);
    void createAppointment(NewAppointment newAppointment);

    void updateAppointmentDiagnosis(AppointmentDiagnosisUpdate appointmentUpdate);

    void updateAppointmentTreatment(AppointmentUpdate appointmentUpdate);

//    void updateAppointmentDiagnosis(Long appointmentId, String diagnosis);

    List<AppointmentResponse> getAppointmentByRange(Date startTime, Date endTime, UUID doctorId);

    AppointmentResponse getAppointmentById(Long id);

//    ListResponse getAppointmentByCustomerId(Long id);

    void cancelAppointment(Long appointmentId, String reason);

    ListResponse getAppointmentByCustomerId(Long id);

}
