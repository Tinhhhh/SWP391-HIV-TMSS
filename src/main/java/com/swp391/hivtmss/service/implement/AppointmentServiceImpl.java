package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Appointment;
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

    @Override
    public List<DoctorResponse> getAvailableDoctors(Date startTime) {

        List<Account> doctors = accountRepository.findByRoleIdAndIsLocked(2L, false);

        if (doctors.isEmpty()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found");
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        startCalendar.add(Calendar.HOUR_OF_DAY, 1);

        // Lấy danh sách cuộc hẹn trong khoảng thời gian từ startTime đến 1 giờ sau
        List<Appointment> appointments = appointmentRepository.findByStartTimeBetween(
                startTime,
                startCalendar.getTime());

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
                doctorResponses.add(doctorResponse);
            }
        }

        return doctorResponses;
    }

    @Override
    @Transactional
    public void createAppointment(NewAppointment newAppointment) {
        Account customer = accountRepository.findById(newAppointment.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Account doctor = accountRepository.findById(newAppointment.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        //Validate
        appointmentValidate(newAppointment);

        Appointment appointment = modelMapper.map(newAppointment, Appointment.class);
        appointmentRepository.save(appointment);
    }

    private void appointmentValidate(NewAppointment newAppointment) {

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


    }

    @Override
    public void updateAppointment(UpdateAppointment updateAppointment) {

    }

}
