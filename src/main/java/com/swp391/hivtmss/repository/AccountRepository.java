package com.swp391.hivtmss.repository;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByEmail(String email);

    List<Account> findByRoleId(long l);

    List<Account> findByRoleIdAndIsLocked(Long roleId, boolean locked);

    Account findByRoleId(Role role);

    Long countByCreatedDateBetween(Date createdDateAfter, Date createdDateBefore);

    Long countByCreatedDateBetweenAndRole_Id(Date start, Date end, long l);

    Long countByCreatedDateBetweenAndRole_IdAndIsActive(Date start, Date end, long l, boolean b);
}


