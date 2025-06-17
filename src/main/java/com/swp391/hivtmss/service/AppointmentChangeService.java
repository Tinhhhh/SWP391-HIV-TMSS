package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import com.swp391.hivtmss.model.payload.response.AppointmentChangeResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;

import java.util.Date;
import java.util.UUID;

public interface AppointmentChangeService {

    void changeAppointmentSlot(Long appointmentId, UUID doctorId, String reason);

    ListResponse getAllAppointmentChangeSendRequest(
            int pageNo, int pageSize, String sortBy, String sortDir, UUID doctorId, Date startTime, Date endTime
    );

    ListResponse getAllAppointmentChangeReceivedRequest(
            int pageNo, int pageSize, String sortBy, String sortDir, UUID doctorId, Date startTime, Date endTime
    );

    void updateAppointmentChangeStatus(Long appointmentChangeId, AppointmentChangeStatus status);

    AppointmentChangeResponse getAppointmentChangeById(Long appointmentChangeId);
}
