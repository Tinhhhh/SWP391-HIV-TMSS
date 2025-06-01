package com.swp391.hivtmss.repository;


import com.swp391.hivtmss.model.entity.DoctorDegree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorDegreeRepository extends JpaRepository<DoctorDegree, Long> {
    Optional<DoctorDegree> findByAccountId(UUID accountId);

}
