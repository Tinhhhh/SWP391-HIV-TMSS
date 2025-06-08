package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.TestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTypeRepository extends JpaRepository<TestType, Long> {
}
