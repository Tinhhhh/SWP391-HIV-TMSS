package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.DegreeImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface DegreeImgRepository extends JpaRepository<DegreeImg, Long> {
    // Find all images for a specific doctor degree
    List<DegreeImg> findByDoctorDegreeId(Long doctorDegreeId);

    // Delete all images for a specific doctor degree
    @Transactional
    void deleteByDoctorDegreeId(Long doctorDegreeId);

    // Check if any images exist for a specific doctor degree
    boolean existsByDoctorDegreeId(Long doctorDegreeId);

    // Count images for a specific doctor degree
    long countByDoctorDegreeId(Long doctorDegreeId);

    @Transactional
    void deleteByDoctorDegreeIdAndImgUrl(Long doctorDegreeId, String imgUrl);
}
