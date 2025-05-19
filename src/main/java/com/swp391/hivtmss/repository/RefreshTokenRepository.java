package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);

    List<RefreshToken> findAllByExpiredOrRevoked(boolean b, boolean b1);

    RefreshToken findByJitId(UUID jitId);

    boolean existsByToken(String refreshToken);
}
