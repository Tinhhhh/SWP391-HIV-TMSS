package com.swp391.hivtmss.util;


import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Blog;
import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@UtilityClass
public class BlogSpecification {

    public Specification<Blog> hasEmailOrFullName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            Join<Blog, Account> accountJoin = root.join("account"); // Tên biến trong entity Blog (kiểu Account)

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(accountJoin.get("email")), likePattern)
//                    cb.like(cb.lower(accountJoin.get("firstName")), likePattern),
//                    cb.like(cb.lower(accountJoin.get("lastName")), likePattern)
            );
        };
    }

    public Specification<Blog> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return null;
            }
            Join<Blog, Account> accountJoin = root.join("account");
            String likePattern = phoneNumber + "%";

            return cb.like(cb.lower(accountJoin.get("phone")), likePattern);
        };
    }

    public Specification<Blog> hasCustomerId(UUID customerId) {
        return (root, query, cb) -> {
            if (customerId == null) {
                return null;
            }
            Join<Blog, Account> accountJoin = root.join("account");
            return cb.equal(accountJoin.get("id"), customerId);
        };
    }
}
