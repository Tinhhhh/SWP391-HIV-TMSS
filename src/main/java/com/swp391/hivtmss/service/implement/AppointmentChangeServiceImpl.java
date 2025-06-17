package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.AppointmentChange;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.response.AppointmentChangeResponse;
import com.swp391.hivtmss.model.payload.response.DoctorResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.AppointmentChangeRepository;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.service.AppointmentChangeService;
import com.swp391.hivtmss.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentChangeServiceImpl implements AppointmentChangeService {

    private final AppointmentChangeRepository appointmentChangeRepository;
    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;


    @Override
    public void changeAppointmentSlot(Long appointmentId, UUID doctorId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        //Bác sĩ mới phải khác bác sĩ cũ
        if (appointment.getDoctor().getId().equals(doctorId)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is the same as the current appointment doctor");
        }

        // Cuộc hẹn phải đang ở trạng thái PENDING
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment is not in pending status");
        }

        // Cuộc hẹn phải chưa bắt đầu
        if (appointment.getStartTime().before(DateUtil.getCurrentTimestamp())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment is already past the scheduled time");
        }

        // Bác sĩ mới phải rảnh trong khoảng thời gian này
        boolean hasAppointment = appointmentRepository.findByStartTime(appointment.getStartTime())
                .stream().anyMatch(a -> a.getDoctor().getId().equals(doctorId) && a.getStatus().equals(AppointmentStatus.PENDING));

        if (hasAppointment) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor already has an appointment at this time");
        }

        // Lưu lại bác sĩ cũ để tạo lịch sử thay đổi cuộc hẹn
        Account oldDoctor = appointment.getDoctor();

        // Bác sĩ mới phải có role là DOCTOR
        Account newDoctor = accountRepository.findById(doctorId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found"));

        if (!newDoctor.isActive() || !newDoctor.getRole().getRoleName().equals(RoleName.DOCTOR.toString())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is not valid or not active");
        }

        AppointmentChange appointmentChange = new AppointmentChange();
        appointmentChange.setAppointment(appointment);
        appointmentChange.setReason(reason);
        appointmentChange.setStatus(AppointmentChangeStatus.PENDING);
        appointmentChange.setOldDoctor(oldDoctor);
        appointmentChange.setNewDoctor(newDoctor);
        appointmentChangeRepository.save(appointmentChange);

        // Tạo notification cho bác sĩ và khách hàng

    }

    @Override
    public ListResponse getAllAppointmentChangeSendRequest(int pageNo, int pageSize, String sortBy, String sortDir, UUID doctorId, Date startTime, Date endTime) {

        // Kiểm tra xem người dùng có phải là bác sĩ không
        Account doctor = accountRepository.findById(doctorId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found"));

        if (!doctor.isActive() || !doctor.getRole().getRoleName().equals(RoleName.DOCTOR.toString())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is not valid or not active");
        }

        // Lấy danh sách lịch sử cuộc hẹn của bác sĩ
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (endTime.before(startTime)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, end time must be after start time");
        }

        Page<AppointmentChange> appointmentChangePage = appointmentChangeRepository.findByOldDoctor_IdAndCreatedDateBetween(doctorId, startTime, endTime, pageable);

        return getAppointmentChangesResponse(pageNo, pageSize, appointmentChangePage);
    }

    @Override
    public ListResponse getAllAppointmentChangeReceivedRequest(int pageNo, int pageSize, String sortBy, String sortDir, UUID doctorId, Date startTime, Date endTime) {
        // Kiểm tra xem người dùng có phải là bác sĩ không
        Account doctor = accountRepository.findById(doctorId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found"));

        if (!doctor.isActive() || !doctor.getRole().getRoleName().equals(RoleName.DOCTOR.toString())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is not valid or not active");
        }

        // Lấy danh sách lịch sử cuộc hẹn của bác sĩ
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        if (endTime.before(startTime)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, end time must be after start time");
        }

        Page<AppointmentChange> appointmentChangePage = appointmentChangeRepository.findByNewDoctor_IdAndCreatedDateBetween(doctorId, startTime, endTime, pageable);

        return getAppointmentChangesResponse(pageNo, pageSize, appointmentChangePage);
    }

    private ListResponse getAppointmentChangesResponse(int pageNo, int pageSize, Page<AppointmentChange> appointmentChangePage) {
        List<AppointmentChangeResponse> appointmentChangeResponseList = appointmentChangePage.getContent().stream()
                .map(this::getAppointmentChangeResponse).toList();


        return new ListResponse(appointmentChangeResponseList,
                pageNo,
                pageSize,
                appointmentChangePage.getTotalElements(),
                appointmentChangePage.getTotalPages(),
                appointmentChangePage.isLast());
    }

    private AppointmentChangeResponse getAppointmentChangeResponse(AppointmentChange appointmentChange) {
        AppointmentChangeResponse response = restrictedModelMapper.map(appointmentChange, AppointmentChangeResponse.class);
        response.setAppointmentId(appointmentChange.getAppointment().getId());
        response.setAppointmentChangeId(appointmentChange.getId());

        DoctorResponse oldDoctorResponse = modelMapper.map(appointmentChange.getOldDoctor(), DoctorResponse.class);
        oldDoctorResponse.setFullName(appointmentChange.getOldDoctor().getLastName() + " " + appointmentChange.getOldDoctor().getFirstName());

        DoctorResponse newDoctorResponse = modelMapper.map(appointmentChange.getNewDoctor(), DoctorResponse.class);
        newDoctorResponse.setFullName(appointmentChange.getNewDoctor().getLastName() + " " + appointmentChange.getNewDoctor().getFirstName());

        response.setOldDoctor(oldDoctorResponse);
        response.setNewDoctor(newDoctorResponse);
        return response;
    }

    @Override
    @Transactional
    public void updateAppointmentChangeStatus(Long appointmentChangeId, AppointmentChangeStatus status) {

        AppointmentChange appointmentChange = appointmentChangeRepository.findById(appointmentChangeId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment change not found"));

        Appointment appointment = appointmentChange.getAppointment();

        // Chỉ có thể thay đổi trạng thái của lịch sử cuộc hẹn nếu nó đang ở trạng thái PENDING
        if (appointmentChange.getStatus() != AppointmentChangeStatus.PENDING) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment change is not in pending status");
        }

        if (status == AppointmentChangeStatus.CANCELLED || status == AppointmentChangeStatus.PENDING) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, invalid status");
        }

        // Nếu status là Accepted, cập nhật cuộc hẹn với bác sĩ mới
        if (status == AppointmentChangeStatus.ACCEPTED) {
            appointment.setDoctor(appointmentChange.getNewDoctor());
            appointmentRepository.save(appointment);

        }

        // Nếu status là Rejected, không cần làm gì thêm, chỉ cần cập nhật trạng thái của lịch sử cuộc hẹn
        appointmentChange.setStatus(status);
        appointmentChangeRepository.save(appointmentChange);

    }

    @Override
    public AppointmentChangeResponse getAppointmentChangeById(Long appointmentChangeId) {

        AppointmentChange appointmentChange = appointmentChangeRepository.findById(appointmentChangeId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment change not found"));

        return getAppointmentChangeResponse(appointmentChange);
    }

    @Scheduled(cron = "1 15 0 * * ?")
    public void updateAppointmentChangeStatus() {
        // Lấy danh sách tất cả các cuộc hẹn có trạng thái PENDING và đã qua thời gian hẹn
        Date now = DateUtil.getCurrentTimestamp();
        List<AppointmentChange> pendingAppointmentChange = appointmentChangeRepository.findByStatusAndCreatedDateBefore(AppointmentChangeStatus.PENDING, now);

        if (!pendingAppointmentChange.isEmpty()) {
            // Duyệt qua từng appointment và cập nhật status
            for (AppointmentChange appointment : pendingAppointmentChange) {
                appointment.setStatus(AppointmentChangeStatus.CANCELLED);
                appointmentChangeRepository.save(appointment);
            }
        }

    }

}
