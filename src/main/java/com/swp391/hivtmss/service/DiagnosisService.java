package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.AppointmentDiagnosisUpdate;
import com.swp391.hivtmss.model.payload.response.DiagnosisResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;

public interface DiagnosisService {

    DiagnosisResponse getDiagnosisByAppointmentId(Long appointmentId);
    DiagnosisResponse getDiagnosisById(Long diagnosisId);

    void editDiagnosis(Long appointmentId, AppointmentDiagnosisUpdate update);

}
