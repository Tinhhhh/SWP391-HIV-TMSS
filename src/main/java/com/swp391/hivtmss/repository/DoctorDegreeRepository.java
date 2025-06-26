package com.swp391.hivtmss.repository;


import com.swp391.hivtmss.model.entity.DoctorDegree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorDegreeRepository extends JpaRepository<DoctorDegree, Long> {
    Optional<DoctorDegree> findByAccountId(UUID accountId);

}
