package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAccountId(UUID accountId);
}
