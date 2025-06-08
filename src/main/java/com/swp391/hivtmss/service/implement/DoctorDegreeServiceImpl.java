package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.DegreeImg;
import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.DoctorDegreeRequest;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.DegreeImgRepository;
import com.swp391.hivtmss.repository.DoctorDegreeRepository;
import com.swp391.hivtmss.service.CloudinaryService;
import com.swp391.hivtmss.service.DoctorDegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Thêm import này
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorDegreeServiceImpl implements DoctorDegreeService {
    private final DoctorDegreeRepository doctorDegreeRepository;
    private final AccountRepository accountRepository;
    private final CloudinaryService cloudinaryService;
    private final DegreeImgRepository degreeImgRepository;

    @Override
    @Transactional
    public DoctorDegreeResponse createDoctorDegree(DoctorDegreeRequest doctorDegreeRequest) {
        Account account = accountRepository.findById(doctorDegreeRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));


        if (doctorDegreeRepository.findByAccountId(account.getId()).isPresent()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Doctor degree already exists for this account");
        }

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

        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

    @Override
    public DoctorDegreeResponse getDoctorDegreeById(Long id) {
        return convertToResponse(getDoctorDegreeEntityById(id));
    }

    @Override
    public DoctorDegreeResponse getDoctorDegreeByAccountId(UUID accountId) {
        return convertToResponse(doctorDegreeRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found for this account")));
    }

    @Override
    public List<DoctorDegreeResponse> getAllDoctorDegrees() {
        return doctorDegreeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorDegreeResponse updateDoctorDegree(Long id, DoctorDegreeRequest request) {
        DoctorDegree doctorDegree = getDoctorDegreeEntityById(id);

        doctorDegree.setName(request.getName());
        doctorDegree.setDob(request.getDob());
        doctorDegree.setGraduationDate(request.getGraduationDate());
        doctorDegree.setClassification(request.getClassification());
        doctorDegree.setStudyMode(request.getStudyMode());
        doctorDegree.setIssueDate(request.getIssueDate());
        doctorDegree.setSchoolName(request.getSchoolName());
        doctorDegree.setRegNo(request.getRegNo());

        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

    @Override
    @Transactional
    public void deleteDoctorDegree(Long id) {
        DoctorDegree doctorDegree = getDoctorDegreeEntityById(id);

        // First delete images from cloud storage
        for (DegreeImg image : doctorDegree.getDegreeImgs()) {
            try {
                cloudinaryService.deleteFile(image.getImgUrl());
            } catch (IOException e) {
                throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to delete image from storage: " + e.getMessage());
            }
        }

        // Remove all images
        doctorDegree.getDegreeImgs().clear();

        // Handle bidirectional relationship with Account
        Account account = doctorDegree.getAccount();
        if (account != null) {
            account.setDoctorDegree(null);
        }

        // Delete the doctor degree
        doctorDegreeRepository.delete(doctorDegree);
    }

    @Override
    public DoctorDegree getDoctorDegreeEntityById(Long id) {
        return doctorDegreeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor degree not found"));
    }

    @Override
    @Transactional
    public DoctorDegreeResponse saveDoctorDegree(DoctorDegree doctorDegree) {
        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

    @Override
    @Transactional
    public DoctorDegreeResponse uploadDegreeImages(Long id, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "No files provided");
        }

        DoctorDegree doctorDegree = getDoctorDegreeEntityById(id);

        List<DegreeImg> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image/")) {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Invalid file type. Only images are allowed");
            }

            try {
                String imageUrl = cloudinaryService.uploadFile(file);
                DegreeImg degreeImg = new DegreeImg();
                degreeImg.setImgUrl(imageUrl);
                degreeImg.setDoctorDegree(doctorDegree);
                images.add(degreeImg);
            } catch (IOException e) {
                throw new HivtmssException(HttpStatus.BAD_REQUEST, "Failed to upload image: " + e.getMessage());
            }
        }

        doctorDegree.getDegreeImgs().addAll(images);
        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

    private DoctorDegreeResponse convertToResponse(DoctorDegree doctorDegree) {
        DoctorDegreeResponse response = new DoctorDegreeResponse(
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

        if (doctorDegree.getDegreeImgs() != null && !doctorDegree.getDegreeImgs().isEmpty()) {
            response.setImageUrls(doctorDegree.getDegreeImgs().stream()
                    .map(DegreeImg::getImgUrl)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    @Override
    @Transactional
    public DoctorDegreeResponse deleteAllImages(Long doctorDegreeId) {
        DoctorDegree doctorDegree = getDoctorDegreeEntityById(doctorDegreeId);

        // Delete from Cloudinary
        for (DegreeImg image : doctorDegree.getDegreeImgs()) {
            try {
                cloudinaryService.deleteFile(image.getImgUrl());
            } catch (IOException e) {
                throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to delete image from storage: " + e.getMessage());
            }
        }

        // Clear images from database
        degreeImgRepository.deleteByDoctorDegreeId(doctorDegreeId);
        doctorDegree.getDegreeImgs().clear();

        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

    @Override
    @Transactional
    public DoctorDegreeResponse deleteImageByUrl(Long doctorDegreeId, String imageUrl) {
        DoctorDegree doctorDegree = getDoctorDegreeEntityById(doctorDegreeId);

        // Verify image exists
        boolean imageExists = doctorDegree.getDegreeImgs().stream()
                .anyMatch(img -> img.getImgUrl().equals(imageUrl));

        if (!imageExists) {
            throw new ResourceNotFoundException("Image not found");
        }

        // Delete from Cloudinary
        try {
            cloudinaryService.deleteFile(imageUrl);
        } catch (IOException e) {
            throw new HivtmssException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete image from storage: " + e.getMessage());
        }

        // Delete from database
        degreeImgRepository.deleteByDoctorDegreeIdAndImgUrl(doctorDegreeId, imageUrl);
        doctorDegree.getDegreeImgs().removeIf(img -> img.getImgUrl().equals(imageUrl));

        return convertToResponse(doctorDegreeRepository.save(doctorDegree));
    }

}
