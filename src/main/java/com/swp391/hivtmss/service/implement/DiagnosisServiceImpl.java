package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.Diagnosis;
import com.swp391.hivtmss.model.payload.enums.AppointmentStatus;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.response.DiagnosisResponse;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.repository.DiagnosisRepository;
import com.swp391.hivtmss.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper restrictedModelMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DiagnosisResponse getDiagnosisByAppointmentId(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        return restrictedModelMapper.map(appointment.getDiagnosis(), DiagnosisResponse.class);
    }

    @Override
    public DiagnosisResponse getDiagnosisById(Long diagnosisId) {

        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, diagnosis not found"));

        return restrictedModelMapper.map(diagnosis, DiagnosisResponse.class);
    }

    @Override
    public void editDiagnosis(Long appointmentId, AppointmentDiagnosisUpdate update) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment not found"));

        if (appointment.getDiagnosis() == null) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, diagnosis not found");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, appointment is not in pending status");
        }

        Diagnosis diagnosis = appointment.getDiagnosis();
        restrictedModelMapper.map(update, diagnosis);
        diagnosisRepository.save(diagnosis);

    }
}
