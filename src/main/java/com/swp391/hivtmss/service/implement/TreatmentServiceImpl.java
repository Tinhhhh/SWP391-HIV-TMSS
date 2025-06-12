package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Appointment;
import com.swp391.hivtmss.model.entity.Treatment;
import com.swp391.hivtmss.model.entity.TreatmentRegimenDrug;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.response.DrugResponse;
import com.swp391.hivtmss.model.payload.response.TreatmentResponse;
import com.swp391.hivtmss.repository.AppointmentRepository;
import com.swp391.hivtmss.repository.TreatmentRegimenDrugRepository;
import com.swp391.hivtmss.repository.TreatmentRepository;
import com.swp391.hivtmss.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final ModelMapper restrictedModelMapper;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;

    @Override
    public TreatmentResponse getTreatmentByAppointmentId(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        return getTreatmentResponse(appointment.getTreatment());
    }

    @Override
    public TreatmentResponse getTreatmentById(Long treatmentId) {

        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found with ID: " + treatmentId));

        return getTreatmentResponse(treatment);
    }

    private TreatmentResponse getTreatmentResponse(Treatment treatment) {

        if (treatment == null) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, treatment not found");
        }

        TreatmentResponse modelResponse = restrictedModelMapper.map(treatment, TreatmentResponse.class);

        List<TreatmentRegimenDrug> treatmentRegimenDrugList = treatmentRegimenDrugRepository.findByTreatmentRegimen_IdAndMethod(
                treatment.getTreatmentRegimen().getId(), treatment.getMethod());

        List<DrugResponse> drugResponses = treatmentRegimenDrugList.stream()
                .map(trd -> restrictedModelMapper.map(trd.getDrug(), DrugResponse.class))
                .toList();

        modelResponse.setName(treatment.getTreatmentRegimen().getName());
        modelResponse.setTreatmentId(treatment.getId());
        modelResponse.setTreatmentRegimenId(treatment.getTreatmentRegimen().getId());
        modelResponse.setDrugs(drugResponses);

        return modelResponse;
    }
}
