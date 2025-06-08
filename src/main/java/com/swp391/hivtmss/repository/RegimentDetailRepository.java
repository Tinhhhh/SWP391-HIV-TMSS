package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.RegimenDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimentDetailRepository extends JpaRepository<RegimenDetail, Long> {
}
