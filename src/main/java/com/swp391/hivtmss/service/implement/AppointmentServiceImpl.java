package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.*;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import com.swp391.hivtmss.model.payload.enums.EmailTemplateName;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.request.AppointmentUpdate;
import com.swp391.hivtmss.model.payload.request.NewAppointment;
import com.swp391.hivtmss.model.payload.response.*;
import com.swp391.hivtmss.repository.*;
import com.swp391.hivtmss.service.AppointmentService;
import com.swp391.hivtmss.service.EmailService;
import com.swp391.hivtmss.service.NotificationService;
import com.swp391.hivtmss.util.AppointmentSpecification;
import com.swp391.hivtmss.util.AppointmentTime;
import com.swp391.hivtmss.util.DateUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;
    private final TestTypeRepository testTypeRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final TreatmentRegimenRepository treatmentRegimenRepository;
    private final TreatmentRepository treatmentRepository;
    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final BlogRepository blogRepository;

    @Override
    public List<DoctorResponse> getAvailableDoctors(Date startTime) {

        // Chuyển đổi thời gian từ UTC sang ICT
        startTime = DateUtil.convertUTCtoICT(startTime);

        List<Account> doctors = accountRepository.findByRoleIdAndIsLocked(2L, false);

        if (doctors.isEmpty()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found");
        }

        // Lấy danh sách cuộc hẹn trong khoảng thời gian từ startTime đến 1 giờ sau
        List<Appointment> appointments = appointmentRepository.findByStartTime(startTime)
                .stream().filter(appointment -> appointment.getStatus().equals(AppointmentStatus.PENDING)).toList();

        final List<DoctorResponse> doctorResponses = new ArrayList<>();
        // Nếu không có cuộc hẹn nào trong khoảng thời gian này, trả về tất cả bác sĩ
        List<Account> availableDoctors = doctors;
        if (!appointments.isEmpty()) {
            // Nếu có cuộc hẹn, lọc ra những bác sĩ không có cuộc hẹn trong khoảng thời gian này

            // Lấy danh sách bác sĩ đã có cuộc hẹn
            Set<Account> unavailableDoctors = appointments.stream()
                    .map(Appointment::getDoctor)
                    .collect(Collectors.toSet());

            // Lấy danh sách bác sĩ không có cuộc hẹn trong khoảng thời gian này
            availableDoctors = doctors.stream()
                    .filter(doctor -> !unavailableDoctors.contains(doctor))
                    .toList();

        }

        for (Account doctor : availableDoctors) {
            DoctorResponse doctorResponse = modelMapper.map(doctor, DoctorResponse.class);
            doctorResponse.setFullName(doctor.fullName());
            doctorResponses.add(doctorResponse);
        }

        return doctorResponses;
    }

    @Override
    @Transactional
    public void createAppointment(NewAppointment newAppointment) {
        Account customer = accountRepository.findById(newAppointment.getCustomerId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, customer not found"));

        if (!Objects.equals(customer.getRole().getRoleName(), RoleName.CUSTOMER.getRole())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, customer is not valid");
        }

        Account doctor = accountRepository.findById(newAppointment.getDoctorId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor not found"));

        if (!Objects.equals(doctor.getRole().getRoleName(), RoleName.DOCTOR.getRole())) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, doctor is not valid");
        }
        //Validate
        appointmentValidate(newAppointment);

        Appointment appointment = restrictedModelMapper.map(newAppointment, Appointment.class);
        appointment.setCustomer(customer);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNextFollowUpReminder(false);

        appointment = appointmentRepository.save(appointment);

        // Tạo notification cho bác sĩ và khách hàng
        notificationService.newAppointment(appointment.getId());

    }

    private void appointmentValidate(NewAppointment newAppointment) {

        newAppointment.setStartTime(DateUtil.convertUTCtoICT(newAppointment.getStartTime()));
        newAppointment.setEndTime(DateUtil.convertUTCtoICT(newAppointment.getEndTime()));

        LocalDateTime startDate = DateUtil.convertToLocalDateTime(newAppointment.getStartTime());
        LocalDateTime workingTimeStart = DateUtil.getLocalDateTime(startDate, AppointmentTime.WORKING_HOURS_START);
        LocalDateTime workingTimeEnd = DateUtil.getLocalDateTime(startDate, AppointmentTime.BOOKING_HOURS_END);

        // Kiểm tra ngày hẹn có nằm trong 5 ngày làm việc
        if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Không thể đặt lịch hẹn vào ngày cuối tuần, vui lòng chọn ngày trong tuần (Thứ Hai - Thứ Sáu)");
        }

        // Kiểm tra giờ làm việc có nằm trong khoảng từ 8h sáng đến 4h chiều
        if (startDate.isBefore(workingTimeStart) ||
                startDate.isAfter(workingTimeEnd)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Không thể đặt lịch hẹn ngoài giờ làm việc, vui lòng chọn giờ trong khoảng từ 8h sáng đến 4h chiều");
        }

        // Kiểm tra giờ bắt đầu phải là giờ hiện tại hoặc sau giờ hiện tại
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Bạn không thể đặt lịch hẹn trong quá khứ, vui lòng chọn giờ trong tương lai");
        }

        //Kiểm tra bác sĩ có lịch hẹn trong khoảng thời gian này hay không

        Optional<Appointment> appointments = appointmentRepository.findByStartTimeBetweenAndDoctor_Id(
                newAppointment.getStartTime(),
                newAppointment.getDoctorId()).stream().filter(appointment -> appointment.getStatus().equals(AppointmentStatus.PENDING)).findFirst();

        if (appointments.isPresent()) {
            throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR, "Bác sĩ đã có lịch hẹn trong khoảng thời gian này, vui lòng chọn thời gian khác");
        }

        //Kiểm tra xem trong ngày hôm nay khách hàng này đã đặt lịch hẹn nào chưa
        Date startOfDay = DateUtil.convertToStartOfTheDay(new Date());
        Date endOfDay = DateUtil.convertToEndOfTheDay(new Date());
        Optional<Appointment> customerAppointments = appointmentRepository
                .findByStartTimeBetweenAndCustomer_IdAndStatus(startOfDay, endOfDay, newAppointment.getCustomerId(), AppointmentStatus.PENDING);

        if (customerAppointments.isPresent()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Bạn đã có một lịch hẹn trong ngày hôm nay nên không thể đặt lịch hẹn thêm nữa");
        }


    }

    @Override
    @Transactional
    public void updateAppointmentDiagnosis(AppointmentDiagnosisUpdate appointmentUpdate) throws MessagingException {

        Appointment appointment = appointmentRepository.findById(appointmentUpdate.getAppointmentId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        TestType testType = testTypeRepository.findById(appointmentUpdate.getTestTypeId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, test type not found"));

        Diagnosis diagnosis = restrictedModelMapper.map(appointmentUpdate, Diagnosis.class);
        diagnosis.setTestType(testType);
        diagnosisRepository.save(diagnosis);

        appointment.setDiagnosis(diagnosis);

        if (appointmentUpdate.isFinished()) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setNextFollowUpReminder(true);

            // Tạo notification cho bác sĩ và khách hàng
            notificationService.appointmentFinished(appointment.getId());
            // Gửi email thông báo cho khách hàng
            emailService.sendAppointmentFinishedNotification(
                    appointment.getCustomer().fullName(),
                    appointment.getCustomer().getEmail(),
                    appointment,
                    EmailTemplateName.APPOINTMENT_FINISHED.getName(),
                    "[HIV TMSS service] Thông báo kết quả xét nghiệm và điều trị HIV"
            );
        }

        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void updateAppointmentTreatment(AppointmentUpdate appointmentUpdate) throws MessagingException {
        Appointment appointment = appointmentRepository.findById(appointmentUpdate.getAppointmentId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        TreatmentRegimen treatmentRegimen = treatmentRegimenRepository.findById(appointmentUpdate.getTreatmentRegimenId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, regiment detail not found"));

        restrictedModelMapper.map(appointmentUpdate, appointment);

        Treatment treatment = new Treatment();

        //Validate treatment method
        List<TreatmentRegimenDrug> listDrug = treatmentRegimenDrugRepository.findByTreatmentRegimen_IdAndMethod(treatmentRegimen.getId(), appointmentUpdate.getMethod());

        if (listDrug.isEmpty()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, treatment method not found in treatment regimen");
        } else {
            // Kiểm tra xem phương pháp điều trị có hợp lệ trong phác đồ điều trị không
            boolean hasMethod = listDrug.stream()
                    .anyMatch(drug -> drug.getMethod() == appointmentUpdate.getMethod());

            if (!hasMethod) {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, treatment method is not valid for this treatment regimen");
            }
        }


        treatment.setMethod(appointmentUpdate.getMethod());
        treatment.setTreatmentRegimen(treatmentRegimen);
        treatment.setDosageInstruction(appointmentUpdate.getDosageInstruction());
        treatmentRepository.save(treatment);

        appointment.setTreatment(treatment);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setNextFollowUp(DateUtil.convertToStartOfTheDay(appointmentUpdate.getNextFollowUp()));
        appointmentRepository.save(appointment);

        // Tạo notification cho bác sĩ và khách hàng
        notificationService.appointmentFinished(appointment.getId());
        // Gửi email thông báo cho khách hàng
        emailService.sendAppointmentFinishedNotification(
                appointment.getCustomer().fullName(),
                appointment.getCustomer().getEmail(),
                appointment,
                EmailTemplateName.APPOINTMENT_FINISHED.getName(),
                "[HIV TMSS service] Thông báo kết quả xét nghiệm và điều trị HIV"
        );


    }

    @Override
    public List<AppointmentResponse> getAppointmentByRange(Date startTime, Date endTime, UUID doctorId) {

        List<Appointment> appointments = appointmentRepository.findByStartTimeBetweenAndDoctor_Id(startTime, endTime, doctorId);
        List<AppointmentResponse> listResponse = new ArrayList<>();
        if (!appointments.isEmpty()) {
            for (Appointment appointment : appointments) {
                AppointmentResponse response = getAppointmentResponse(appointment, appointment.isAnonymous());
                listResponse.add(response);
            }
        } else {
            throw new HivtmssException(HttpStatus.OK, "Request accepted, no appointment found in this time range");
        }
        return listResponse;
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        return getAppointmentResponse(appointment, appointment.isAnonymous());
    }

    @Override
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment is not in pending status");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelReason(reason);
        appointmentRepository.save(appointment);
    }

    @Override
    public ListResponse getAppointmentByCustomerId(int pageNo, int pageSize, String sortBy, String sortDir, UUID customerId) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Appointment> spec = Specification.where(AppointmentSpecification.hasCustomerId(customerId));

        return getAppointmentResponseWithPagination(pageNo, pageSize, pageable, spec);
    }

    private ListResponse getAppointmentResponseWithPagination(int pageNo, int pageSize, Pageable pageable, Specification<Appointment> spec) {
        Page<Appointment> appointments = appointmentRepository.findAll(spec, pageable);

        List<AppointmentResponse> listResponse = new ArrayList<>();

        if (!appointments.isEmpty()) {
            for (Appointment appointment : appointments) {
                AppointmentResponse response = getAppointmentResponse(appointment, false);
                listResponse.add(response);
            }
        }

        return new ListResponse(listResponse, pageNo, pageSize, appointments.getTotalElements(),
                appointments.getTotalPages(), appointments.isLast());
    }

    @Override
    public ListResponse getAllAppointment(int pageNo, int pageSize, String sortBy, String sortDir, String searchTerm) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Appointment> spec = Specification.where(AppointmentSpecification.hasEmailOrFullName(searchTerm)
                .or(AppointmentSpecification.hasPhoneNumber(searchTerm)));

        return getAppointmentResponseWithPagination(pageNo, pageSize, pageable, spec);
    }


    private AppointmentResponse getAppointmentResponse(Appointment appointment, boolean isAnonymous) {
        AppointmentResponse response = restrictedModelMapper.map(appointment, AppointmentResponse.class);
        response.setStartTime(DateUtil.formatTimestamp(appointment.getStartTime()));

        if (appointment.getEndTime() != null) {
            response.setEndTime(DateUtil.formatTimestamp(appointment.getEndTime()));
        }

        response.setCreatedDate(DateUtil.formatTimestamp(appointment.getCreatedDate()));

        // Set doctor information
        DoctorResponse doctor = restrictedModelMapper.map(appointment.getDoctor(), DoctorResponse.class);
        doctor.setFullName(appointment.getDoctor().fullName());
        response.setDoctor(doctor);

        // Set customer information
        CustomerResponse customer = restrictedModelMapper.map(appointment.getCustomer(), CustomerResponse.class);
        customer.setFullName(appointment.getCustomer().fullName());
        response.setCustomer(customer);

        response.setAppointmentId(appointment.getId());

        if (isAnonymous) {
            response.setFullName("Bệnh nhân ẩn danh");
        } else {
            response.setFullName(appointment.fullName());
        }

        if (appointment.getDiagnosis() != null) {
            response.setDiagnosisId(appointment.getDiagnosis().getId());
        }

        if (appointment.getTreatment() != null) {
            response.setTreatmentId(appointment.getTreatment().getId());
        }

        Treatment treatment = appointment.getTreatment();
        if (treatment != null) {
            response.setDosage(treatment.getDosageInstruction());
        }

        return response;
    }


    @Scheduled(cron = "1 0 0 * * ?")
    public void updateAppointmentStatus() {
        // Lấy danh sách tất cả các cuộc hẹn có trạng thái PENDING và đã qua thời gian hẹn
        Date now = DateUtil.getCurrentTimestamp();
        List<Appointment> pendingAppointments = appointmentRepository.findByStatusAndStartTimeBefore(AppointmentStatus.PENDING, now);

        if (!pendingAppointments.isEmpty()) {
            // Duyệt qua từng appointment và cập nhật status
            for (Appointment appointment : pendingAppointments) {
                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointment.setCancelReason("Quá thời gian hẹn");
                appointmentRepository.save(appointment);
            }
        }

    }

    @Scheduled(cron = "1 1 0 * * ?")
    public void updateAppointmentChangeHistory() throws MessagingException {
        // Lấy danh sách tất cả các cuộc hẹn có trạng thái COMPLETED và đã qua thời gian hẹn
        Date now = DateUtil.getCurrentTimestamp();
        List<Appointment> completedAppointment = appointmentRepository.findByStatusAndNextFollowUpBeforeAndNextFollowUpReminderFalse(AppointmentStatus.COMPLETED, now);

        if (!completedAppointment.isEmpty()) {
            // Duyệt qua từng appointment và gửi thông báo
            for (Appointment appointment : completedAppointment) {
                emailService.sendAppointmentFollowUpNotification(
                        appointment.getCustomer().fullName(),
                        appointment.getCustomer().getEmail(),
                        DateUtil.formatTimestamp(appointment.getNextFollowUp(), DateUtil.DATE_FORMAT),
                        appointment.fullName(),
                        EmailTemplateName.APPOINTMENT_REMINDER.getName(),
                        "[HIV TMSS service] Bạn có một cuộc hẹn tái khám sắp tới"
                );
                // Cập nhật trạng thái nhắc nhở
                appointment.setNextFollowUpReminder(true);
                appointmentRepository.save(appointment);
            }
        }
    }


    public void testEmail() throws MessagingException {
        Appointment appointment = appointmentRepository.findById(6L)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));
        emailService.sendAppointmentFinishedNotification(
                appointment.getCustomer().fullName(),
                appointment.getCustomer().getEmail(),
                appointment,
                EmailTemplateName.APPOINTMENT_FINISHED.getName(),
                "[HIV TMSS service] Thông báo kết quả xét nghiệm và điều trị HIV"
        );
    }

    @Override
    public DashboardResponse getDashboardByRange(LocalDateTime startDate, LocalDateTime endDate) {

        Date start = DateUtil.convertToStartOfTheDay(DateUtil.convertToDate(startDate));
        Date end = DateUtil.convertToEndOfTheDay(DateUtil.convertToDate(endDate));

        if (end.before(start)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Định dạng thời gian không hợp lệ, ngày kết thúc phải sau ngày bắt đầu");
        }

        DashboardResponse dashboard = getDashboard(start, end);

        return dashboard;
    }

    private DashboardResponse getDashboard(Date start, Date end) {

        Long totalCustomers = accountRepository.countByCreatedDateBetweenAndRole_Id(start, end, 1L);
        Long totalDoctors = accountRepository.countByCreatedDateBetweenAndRole_Id(start, end, 2L);
        Long totalAppointments = appointmentRepository.countByCreatedDateBetween(start, end);
        Long totalBlogs = blogRepository.countByCreatedDateBetween(start, end);

        Long totalCustomersPreviousMonth = accountRepository.countByCreatedDateBetweenAndRole_Id(
                start, DateUtil.subtractOneMonth(end), 1L);

        Long totalAppointmentsPreviousMonth = appointmentRepository.countByCreatedDateBetween(
                start, DateUtil.subtractOneMonth(end));

        double customerRegistrationRate = 0.0;
        if (totalCustomersPreviousMonth != 0) {
            customerRegistrationRate = ((totalCustomers - totalCustomersPreviousMonth) / (double) totalCustomersPreviousMonth) * 100;
            if (!Double.isFinite(customerRegistrationRate)) {
                customerRegistrationRate = 0.0;
            }
        }

        double appointmentRate = 0.0;
        if (totalAppointmentsPreviousMonth != 0) {
            appointmentRate = ((totalAppointments - totalAppointmentsPreviousMonth) / (double) totalAppointmentsPreviousMonth) * 100;
            if (!Double.isFinite(appointmentRate)) {
                appointmentRate = 0.0;
            }
        }

        Long activeDoctors = accountRepository.countByCreatedDateBetweenAndRole_IdAndIsActive(start, end, 2L, true);

        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setTotalCustomers(totalCustomers);
        dashboardResponse.setTotalDoctors(totalDoctors);
        dashboardResponse.setTotalAppointments(totalAppointments);
        dashboardResponse.setTotalBlogs(totalBlogs);
        dashboardResponse.setCustomerRegistrationRate(customerRegistrationRate);
        dashboardResponse.setAppointmentRate(appointmentRate);
        dashboardResponse.setActiveDoctors(activeDoctors);
        dashboardResponse.setStartDate(DateUtil.formatTimestamp(start));
        dashboardResponse.setEndDate(DateUtil.formatTimestamp(end));

        return dashboardResponse;
    }


    @Override
    public ChartResponse getMonthlyDashboardByRange(LocalDateTime startDate, LocalDateTime endDate) {
        Map<Integer, List<LocalDateTime>> result = new LinkedHashMap<>();

        Date start = DateUtil.convertToStartOfTheDay(DateUtil.convertToDate(startDate));
        Date end = DateUtil.convertToEndOfTheDay(DateUtil.convertToDate(endDate));

        if (end.before(start)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "End date must be after start date.");
        }

        LocalDate startLocalDate = DateUtil.convertToLocalDateTime(start).toLocalDate();
        LocalDate endLocalDate = DateUtil.convertToLocalDateTime(end).toLocalDate();


        // Tìm thứ Hai đầu tiên chứa start
        LocalDate currentStart = startLocalDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int weekIndex = 1;

        while (!currentStart.isAfter(endLocalDate)) {
            //Tìm ngày cuối cùng của tuần này
            LocalDate currentEnd = currentStart.plusDays(6);

            //Kiểm tra ngày cuối cùng tuần này có vượt quá end date không
            if (currentEnd.isAfter(endLocalDate)) {
                currentEnd = endLocalDate;
            }

            List<LocalDateTime> range = List.of(
                    currentStart.atStartOfDay(),
                    currentEnd.atTime(LocalTime.MAX)
            );

            result.put(weekIndex++, range);
            currentStart = currentStart.plusWeeks(1);
        }

        ChartResponse chartResponse = new ChartResponse();
        List<DashboardResponse> dashboardResponses = new ArrayList<>();
        for (Map.Entry<Integer, List<LocalDateTime>> entry : result.entrySet()) {
            int week = entry.getKey();
            List<LocalDateTime> dateRange = entry.getValue();
            Date startRange = DateUtil.convertToDate(dateRange.get(0));
            Date endRange = DateUtil.convertToDate(dateRange.get(1));
            DashboardResponse dashboardResponse = getDashboard(startRange, endRange);
            chartResponse.setWeek(week);
            dashboardResponses.add(dashboardResponse);
        }

        chartResponse.setWeeklyDashboard(dashboardResponses);

        return chartResponse;
    }

}
