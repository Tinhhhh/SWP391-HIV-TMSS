package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorDegreeService {
    DoctorDegreeResponse createDoctorDegree(DoctorDegreeRequest doctorDegreeRequest);
    DoctorDegreeResponse getDoctorDegreeById(Long id);
    DoctorDegreeResponse getDoctorDegreeByAccountId(UUID accountId);
    List<DoctorDegreeResponse> getAllDoctorDegrees();
    DoctorDegreeResponse updateDoctorDegree(Long id, DoctorDegreeRequest doctorDegreeRequest);
    void deleteDoctorDegree(Long id);
}
