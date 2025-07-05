package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.Notification;
import com.swp391.hivtmss.model.payload.enums.NotificationStatus;
import com.swp391.hivtmss.model.payload.enums.NotificationStatusFilter;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.model.payload.response.NotificationResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.repository.NotificationRepository;
import com.swp391.hivtmss.service.NotificationService;
import com.swp391.hivtmss.util.DateUtil;
import com.swp391.hivtmss.util.NotificationSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final AccountRepository accountRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void newAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Account doctorAccount = accountRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Account customerAccount = accountRepository.findById(appointment.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Notification customerNoti = new Notification();
        customerNoti.setTitle("Bạn có lịch hẹn mới");
        customerNoti.setContent("Lịch hẹn khám bệnh của bạn với bác sĩ " + doctorAccount.fullName() +
                " vào lúc " + DateUtil.formatTimestamp(appointment.getStartTime()) + " đã được tạo thành công."
        );
        customerNoti.setStatus(NotificationStatus.UNREAD);
        customerNoti.setAccount(customerAccount);

        Notification doctorNoti = new Notification();
        doctorNoti.setTitle("Bạn có lịch hẹn mới");

        String patientName = appointment.isAnonymous() ? "bệnh nhân ẩn danh " : "bệnh nhân " + appointment.fullName();

        doctorNoti.setContent("Bạn có lịch hẹn khám bệnh mới với " + patientName +
                " vào lúc " + DateUtil.formatTimestamp(appointment.getStartTime()) + ".");
        doctorNoti.setStatus(NotificationStatus.UNREAD);
        doctorNoti.setAccount(doctorAccount);

        notificationRepository.save(customerNoti);
        notificationRepository.save(doctorNoti);

    }

    @Override
    public void appointmentFinished(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Account doctorAccount = accountRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Account customerAccount = accountRepository.findById(appointment.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Notification customerNoti = new Notification();
        customerNoti.setTitle("Đã có kết quả xét nghiệm");
        customerNoti.setContent("Lịch hẹn khám bệnh của bạn với bác sĩ" + doctorAccount.fullName() +
                " vào lúc" + DateUtil.formatTimestamp(appointment.getStartTime()) + " đã hoàn thành, vui lòng kiểm tra email hoặc vào mục lịch sử khám để xem chi tiết kết quả xét nghiệm và phương hướng điều trị."
        );
        customerNoti.setStatus(NotificationStatus.UNREAD);
        customerNoti.setAccount(customerAccount);
        notificationRepository.save(customerNoti);
    }

    @Override
    public void appointmentDoctorChange(Account oldDoctor, Account newDoctor) {

        Notification doctorNoti = new Notification();
        doctorNoti.setTitle("Yêu cầu chuyển lịch khám");
        doctorNoti.setContent("Bạn yêu cầu chuyển lịch khám đến từ bác sĩ" + oldDoctor.fullName() +
                ", xin vui lòng kiểm tra mục chuyển lịch khám để trả lời.");
        doctorNoti.setStatus(NotificationStatus.UNREAD);
        doctorNoti.setAccount(newDoctor);
    }

    @Override
    public void appointmentChangeReply(Appointment appointment, Account newDoctor, Account oldDoctor, boolean isAccept) {
        if (isAccept) {
            // noti cho khách hàng
            Notification customerNoti = new Notification();
            customerNoti.setTitle("Lịch hẹn của bạn đã được chuyển sang một bác sĩ khác");
            customerNoti.setContent("Vì một số lý do bác sĩ của bạn đã chuyển lịch khám lại cho bác sĩ khác. " +
                    "Xin vui lòng kiểm tra lại lịch khám của bạn để xem thông tin chi tiết.");
            customerNoti.setStatus(NotificationStatus.UNREAD);
            customerNoti.setAccount(appointment.getCustomer());

        }
        Notification oldDoctorNoti = new Notification();
        oldDoctorNoti.setTitle("Bác sĩ " + newDoctor.fullName() + " đã trả lời yêu cầu chuyển lịch khám");
        oldDoctorNoti.setContent("Bác sĩ " + newDoctor.fullName() + " đã " + (isAccept ? "chấp nhận" : "từ chối") +
                "yêu cầu chuyển lịch hẹn. Xin vui lòng kiểm tra yêu cầu chuyển lịch khám của bạn để xem thông tin chi tiết.");
        oldDoctorNoti.setStatus(NotificationStatus.UNREAD);
        oldDoctorNoti.setAccount(oldDoctor);

    }

    @Override
    public ListResponse getAllNotification(int pageNo, int pageSize, String sortBy, String sortDir, UUID accountId, NotificationStatusFilter statusFilter) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Notification> spec = Specification.where(NotificationSpecification.hasStatus(NotificationStatus.UNREAD)
                .and(NotificationSpecification.hasId(accountId)));

        if (statusFilter == NotificationStatusFilter.ALL) {
            spec = Specification.where(NotificationSpecification.hasStatus(NotificationStatus.UNREAD)
                            .or(NotificationSpecification.hasStatus(NotificationStatus.READ)))
                    .and(NotificationSpecification.hasId(accountId)
                    );
        }

        Page<Notification> notifications = notificationRepository.findAll(spec, pageable);
        List<Notification> notificationList = notifications.getContent();

        List<NotificationResponse> notificationResponses = notificationList.stream()
                .map(notification -> {
                    NotificationResponse n = modelMapper.map(notification, NotificationResponse.class);
                    String timeAgo = DateUtil.getTimeAgo(notification.getCreatedDate());
                    n.setTimeAgo(timeAgo);
                    return n;
                })
                .toList();

        ListResponse listResponse = new ListResponse();
        listResponse.setPageNo(pageNo);
        listResponse.setPageSize(pageSize);
        listResponse.setTotalElements(notifications.getTotalElements());
        listResponse.setTotalPages(notifications.getTotalPages());
        listResponse.setContent(notificationResponses);
        listResponse.setLast(notifications.isLast());

        return listResponse;
    }

    @Override
    public void readNotification(Long notificationId, UUID accountId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getAccount().getId().equals(accountId)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Notification does not belong to this account");
        }

        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);

    }

    @Override
    public void deleteNotification(Long notificationId, UUID accountId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getAccount().getId().equals(accountId)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Notification does not belong to this account");
        }

        notification.setStatus(NotificationStatus.HIDDEN);
        notificationRepository.save(notification);
    }

    @Override
    public void readAllNotifications(List<Long> notificationId, UUID accountId) {
        List<Notification> notifications = notificationRepository.findAllById(notificationId);
        if (!notifications.isEmpty()) {
            notifications.stream().filter(notification -> notification.getAccount().getId().equals(accountId) &&
                            notification.getStatus() == NotificationStatus.UNREAD)
                    .forEach(notification -> notification.setStatus(NotificationStatus.READ));

            notificationRepository.saveAll(notifications);
        }
    }


}
