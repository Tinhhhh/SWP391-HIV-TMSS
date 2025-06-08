package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DoctorDegreeService {
    DoctorDegreeResponse createDoctorDegree(DoctorDegreeRequest doctorDegreeRequest);
    DoctorDegreeResponse getDoctorDegreeById(Long id);
    DoctorDegreeResponse getDoctorDegreeByAccountId(UUID accountId);
    List<DoctorDegreeResponse> getAllDoctorDegrees();
    DoctorDegreeResponse updateDoctorDegree(Long id, DoctorDegreeRequest doctorDegreeRequest);
    void deleteDoctorDegree(Long id);
    DoctorDegree getDoctorDegreeEntityById(Long id);
    DoctorDegreeResponse saveDoctorDegree(DoctorDegree doctorDegree);
    DoctorDegreeResponse uploadDegreeImages(Long id, List<MultipartFile> files);
    DoctorDegreeResponse deleteAllImages(Long doctorDegreeId);
    DoctorDegreeResponse deleteImageByUrl(Long doctorDegreeId, String imageUrl);


}
