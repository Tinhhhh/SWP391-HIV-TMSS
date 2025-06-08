package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.DegreeImg;
import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.DoctorDegreeRepository;
import com.swp391.hivtmss.service.CloudinaryService;
import com.swp391.hivtmss.service.DoctorDegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID; // Thêm import này
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorDegreeServiceImpl implements DoctorDegreeService {

    private final DoctorDegreeRepository doctorDegreeRepository;
    private final AccountRepository accountRepository;
    private final CloudinaryService cloudinaryService;


    @Override
    @Transactional
    public DoctorDegreeResponse createDoctorDegree(DoctorDegreeRequest doctorDegreeRequest) {
        Account account = accountRepository.findById(doctorDegreeRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        DoctorDegree doctorDegree = new DoctorDegree();
        doctorDegree.setName(doctorDegreeRequest.getName());
        doctorDegree.setDob(doctorDegreeRequest.getDob());
        doctorDegree.setGraduationDate(doctorDegreeRequest.getGraduationDate());
        doctorDegree.setClassification(doctorDegreeRequest.getClassification());
        doctorDegree.setStudyMode(doctorDegreeRequest.getStudyMode());
        doctorDegree.setIssueDate(doctorDegreeRequest.getIssueDate());
        doctorDegree.setSchoolName(doctorDegreeRequest.getSchoolName());
        doctorDegree.setRegNo(doctorDegreeRequest.getRegNo());
        doctorDegree.setAccount(account);

        DoctorDegree savedDegree = doctorDegreeRepository.save(doctorDegree);
        return convertToResponse(savedDegree);
    }

    @Override
    public DoctorDegreeResponse getDoctorDegreeById(Long id) {
        DoctorDegree doctorDegree = doctorDegreeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found"));
        return convertToResponse(doctorDegree);
    }

    @Override
    public DoctorDegreeResponse getDoctorDegreeByAccountId(UUID accountId) {
        DoctorDegree doctorDegree = doctorDegreeRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found for this account"));
        return convertToResponse(doctorDegree);
    }

    @Override
    public List<DoctorDegreeResponse> getAllDoctorDegrees() {
        List<DoctorDegree> degrees = doctorDegreeRepository.findAll();
        return degrees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorDegreeResponse updateDoctorDegree(Long id, DoctorDegreeRequest doctorDegreeRequest) {
        DoctorDegree doctorDegree = doctorDegreeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found"));

        //Kiem tra acccount rrequest phai role doctor khong
//        if (!doctorDegree.getAccount().getRole().equals(RoleName.DOCTOR.getRole())) {}

        doctorDegree.setName(doctorDegreeRequest.getName());
        doctorDegree.setDob(doctorDegreeRequest.getDob());
        doctorDegree.setGraduationDate(doctorDegreeRequest.getGraduationDate());
        doctorDegree.setClassification(doctorDegreeRequest.getClassification());
        doctorDegree.setStudyMode(doctorDegreeRequest.getStudyMode());
        doctorDegree.setIssueDate(doctorDegreeRequest.getIssueDate());
        doctorDegree.setSchoolName(doctorDegreeRequest.getSchoolName());
        doctorDegree.setRegNo(doctorDegreeRequest.getRegNo());

        DoctorDegree updatedDegree = doctorDegreeRepository.save(doctorDegree);
        return convertToResponse(updatedDegree);
    }

    @Override
    @Transactional
    public void deleteDoctorDegree(Long id) {
        if (!doctorDegreeRepository.existsById(id)) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Doctor degree not found");
        }
        doctorDegreeRepository.deleteById(id);
    }

    @Override
    public DoctorDegree getDoctorDegreeEntityById(Long id) {
        return doctorDegreeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found"));
    }

    @Override
    @Transactional
    public DoctorDegreeResponse saveDoctorDegree(DoctorDegree doctorDegree) {
        DoctorDegree saved = doctorDegreeRepository.save(doctorDegree);
        return convertToResponse(saved);
    }


    private DoctorDegreeResponse convertToResponse(DoctorDegree doctorDegree) {
        return new DoctorDegreeResponse(
                doctorDegree.getId(),
                doctorDegree.getName(),
                doctorDegree.getDob(),
                doctorDegree.getGraduationDate(),
                doctorDegree.getClassification(),
                doctorDegree.getStudyMode(),
                doctorDegree.getIssueDate(),
                doctorDegree.getSchoolName(),
                doctorDegree.getRegNo(),
                doctorDegree.getAccount().getId(),
                doctorDegree.getAccount().getEmail()
        );
    }

    @Override
    @Transactional
    public DoctorDegreeResponse uploadDegreeImages(Long id, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "No files provided");
        }

        DoctorDegree doctorDegree = getDoctorDegreeEntityById(id);

        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image/")) {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Invalid file type. Only images are allowed");
            }

            try {
                String imageUrl = cloudinaryService.uploadFile(file);
                DegreeImg degreeImg = new DegreeImg();
                degreeImg.setImgUrl(imageUrl);
                degreeImg.setDoctorDegree(doctorDegree);
                doctorDegree.getDegreeImgs().add(degreeImg);
            } catch (IOException e) {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Failed to upload image: " + e.getMessage());
            }
        }

        return saveDoctorDegree(doctorDegree);
    }

}
