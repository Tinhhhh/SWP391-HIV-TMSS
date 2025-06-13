package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.response.TreatmentResponse;

public interface TreatmentService {

    TreatmentResponse getTreatmentByAppointmentId(Long appointmentId);

    TreatmentResponse getTreatmentById(Long treatmentId);


}
