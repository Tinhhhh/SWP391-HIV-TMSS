package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    boolean existsByIdIn(Collection<Long> ids);
}

