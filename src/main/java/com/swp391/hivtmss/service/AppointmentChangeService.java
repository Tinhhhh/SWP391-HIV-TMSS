package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatusFilter;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeType;
import com.swp391.hivtmss.model.payload.response.AppointmentChangeResponse;
import com.swp391.hivtmss.model.payload.response.AppointmentChangeResponseForAdmin;
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

    AppointmentChangeResponseForAdmin getAllAppointmentChangeForAdmin(int pageNo, int pageSize, String sortBy, String sortDir, String searchTerm, AppointmentChangeStatusFilter status, AppointmentChangeType type, Date startTime, Date endTime);

    void reviewAppointmentChange(Long appointmentChangeId, boolean isApproved);

}
