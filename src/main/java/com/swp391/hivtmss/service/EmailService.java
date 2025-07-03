package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.entity.Appointment;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendAccountInformation(String name, String email, String password, String to, String template, String subject) throws MessagingException;

    void sendMimeMessageWithHtml(String name, String to, String content, String template, String subject) throws MessagingException;

    void sendAppointmentFollowUpNotification(String name, String to, String appointmentDate, String patientName, String template, String subject) throws MessagingException;
    void sendAppointmentFinishedNotification(String name, String to, Appointment appointment, String template, String subject) throws MessagingException;
}
