package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.DegreeImg;
import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.DoctorDegreeRepository;
import com.swp391.hivtmss.service.DoctorDegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID; // Thêm import này
import java.util.stream.Collectors;

@Service
public class DoctorDegreeServiceImpl implements DoctorDegreeService {

    @Autowired
    private DoctorDegreeRepository doctorDegreeRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        DoctorDegree doctorDegree = doctorDegreeRepository.findByAccountAccountId(accountId)
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
            throw new ResourceNotFoundException("Doctor degree not found");
        }
        doctorDegreeRepository.deleteById(id);
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
                doctorDegree.getAccount().getAccountId(),
                doctorDegree.getAccount().getEmail()
        );
    }
}
