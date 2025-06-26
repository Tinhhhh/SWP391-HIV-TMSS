package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.BlogImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogImgRepository extends JpaRepository<BlogImg, Long> {
    Optional<BlogImg> findByImgUrl(String imageUrl);
}
