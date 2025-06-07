package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.model.payload.request.UpdateAppointment;
import com.swp391.hivtmss.model.payload.response.DoctorResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.service.AppointmentService;
import com.swp391.hivtmss.util.AppointmentTime;
import com.swp391.hivtmss.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;

    @Override
    public List<DoctorResponse> getAvailableDoctors(Date startTime) {

        List<Account> doctors = accountRepository.findByRoleIdAndIsLocked(2L, false);

        if (doctors.isEmpty()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found");
        }

        // Lấy danh sách cuộc hẹn trong khoảng thời gian từ startTime đến 1 giờ sau
        List<Appointment> appointments = appointmentRepository.findByStartTimeBetween(startTime);

        List<DoctorResponse> doctorResponses = new ArrayList<>();

        if (appointments.isEmpty()) {
            // Nếu không có cuộc hẹn nào trong khoảng thời gian này, trả về tất cả bác sĩ
            for (Account doctor : doctors) {
                DoctorResponse doctorResponse = modelMapper.map(doctor, DoctorResponse.class);
                doctorResponses.add(doctorResponse);
            }
        } else {
            // Nếu có cuộc hẹn, lọc ra những bác sĩ không có cuộc hẹn trong khoảng thời gian này

            // Lấy danh sách bác sĩ đã có cuộc hẹn
            Set<Account> unavailableDoctors = appointments.stream()
                    .map(Appointment::getDoctor)
                    .collect(Collectors.toSet());

            // Lấy danh sách bác sĩ không có cuộc hẹn trong khoảng thời gian này
            List<Account> availableDoctors = doctors.stream()
                    .filter(doctor -> !unavailableDoctors.contains(doctor))
                    .toList();

            for (Account doctor : availableDoctors) {
                DoctorResponse doctorResponse = modelMapper.map(doctor, DoctorResponse.class);
                doctorResponse.setFullName(doctor.fullName());
                doctorResponses.add(doctorResponse);
            }
        }

        return doctorResponses;
    }

    @Override
    public void createAppointment(NewAppointment newAppointment) {
        Account customer = accountRepository.findById(newAppointment.getCustomerId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, customer not found"));

        if (!Objects.equals(customer.getRole().getRoleName(), RoleName.CUSTOMER.getRole())){
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, customer is not valid");
        }

        Account doctor = accountRepository.findById(newAppointment.getDoctorId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found"));

        if (!Objects.equals(doctor.getRole().getRoleName(), RoleName.DOCTOR.getRole())){
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is not valid");
        }
        //Validate
        appointmentValidate(newAppointment);

        Appointment appointment = restrictedModelMapper.map(newAppointment, Appointment.class);
        appointment.setCustomer(customer);
        appointment.setDoctor(doctor);

        appointmentRepository.save(appointment);
    }

    private void appointmentValidate(NewAppointment newAppointment) {

        newAppointment.setStartTime(DateUtil.convertUTCtoICT(newAppointment.getStartTime()));

        LocalDateTime startDate = DateUtil.convertToLocalDateTime(newAppointment.getStartTime());
        LocalDateTime workingTimeStart = DateUtil.getLocalDateTime(startDate, AppointmentTime.WORKING_HOURS_START);
        LocalDateTime workingTimeEnd = DateUtil.getLocalDateTime(startDate, AppointmentTime.WORKING_HOURS_END);

        // Kiểm tra ngày hẹn có nằm trong 5 ngày làm việc
        if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment cannot be scheduled on weekends");
        }

        // Kiểm tra giờ làm việc có nằm trong khoảng từ 8h sáng đến 5h chiều
        if (startDate.isBefore(workingTimeStart) ||
                startDate.isAfter(workingTimeEnd)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment start time must be within working hours (8:00 - 17:00)");
        }

        // Kiểm tra giờ bắt đầu phải là giờ hiện tại hoặc sau giờ hiện tại
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment start time must be after current time");
        }

        //Kiểm tra bác sĩ có lịch hẹn trong khoảng thời gian này hay không

        Optional<Appointment> appointments = appointmentRepository.findByStartTimeBetweenAndDoctor_Id(
                newAppointment.getStartTime(),
                newAppointment.getDoctorId());

        if (appointments.isPresent()) {
            throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error, doctor has another appointment at this time");
        }

    }

    @Override
    public void updateAppointment(UpdateAppointment updateAppointment) {

    }

}
