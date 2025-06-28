package com.swp391.hivtmss.util;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Role;
import com.swp391.hivtmss.model.payload.enums.SortByRole;
import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class AccountSpecification {

    public Specification<Account> hasEmailOrFullName(String keyword) {
        return (root, query, cb) -> {

            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("email")), likePattern),
                    cb.like(cb.lower(root.get("lastName")), likePattern),
                    cb.like(cb.lower(root.get("firstName")), likePattern)
            );
        };

    }

    public Specification<Account> hasRoleName(SortByRole roleName) {
        return (root, query, cb) -> {
            Join<Account, Role> roleJoin = root.join("role");

            if (roleName == null || roleName == SortByRole.ALL) {
                return cb.notEqual(roleJoin.get("roleName"), "ADMIN");
            }

            return cb.and(
                    cb.equal(roleJoin.get("roleName"), roleName.toString()),
                    cb.notEqual(roleJoin.get("roleName"), "ADMIN")
            );
        };
    }
}
