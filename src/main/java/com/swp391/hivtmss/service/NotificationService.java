package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.payload.enums.NotificationStatusFilter;
import com.swp391.hivtmss.model.payload.response.ListResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    //When new appointment is created
    void newAppointment(Long appointmentId);

    void appointmentFinished(Long appointmentId);

    void appointmentDoctorChange(Account oldDoctorId, Account newDoctorId);

    void appointmentChangeReply(Appointment appointment, Account newDoctor, Account oldDoctor, boolean isAccept);

    //get all noti
    ListResponse getAllNotification(int pageNo, int pageSize, String sortBy, String sortDir, UUID accountId, NotificationStatusFilter statusFilter);

    //read noti by account id
    void readNotification(Long notificationId, UUID accountId);

    //delete noti by account id
    void deleteNotification(Long notificationId, UUID accountId);

    //read all notifications by account id
    void readAllNotifications(List<Long> notificationId, UUID accountId);




}
