package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.Notification;
import com.swp391.hivtmss.model.payload.enums.NotificationStatus;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.repository.NotificationRepository;
import com.swp391.hivtmss.service.NotificationService;
import com.swp391.hivtmss.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final AccountRepository accountRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void newAppointment(UUID doctorId, UUID customerId, Long appointmentId) {

        Account doctorAccount = accountRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Account customerAccount = accountRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Notification customerNoti = new Notification();
        customerNoti.setTitle("Bạn có lịch hẹn mới");
        customerNoti.setContent("Lịch hẹn khám bệnh của bạn với bác sĩ" + doctorAccount.fullName() +
                " vào lúc" + DateUtil.formatTimestamp(appointment.getStartTime()) + " đã được tạo thành công."
        );
        customerNoti.setStatus(NotificationStatus.UNREAD);
        customerNoti.setAccount(customerAccount);

        Notification doctorNoti = new Notification();
        doctorNoti.setTitle("Bạn có lịch hẹn mới");

        String patientName = appointment.isAnonymous() ? "bệnh nhân ẩn danh" : "bệnh nhân" + appointment.fullName();

        doctorNoti.setContent("Bạn có lịch hẹn khám bệnh mới với" + patientName +
                " vào lúc " + DateUtil.formatTimestamp(appointment.getStartTime()) + ".");
        doctorNoti.setStatus(NotificationStatus.UNREAD);
        doctorNoti.setAccount(doctorAccount);

        notificationRepository.save(customerNoti);
        notificationRepository.save(doctorNoti);

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
