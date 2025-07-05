package com.swp391.hivtmss.util;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.AppointmentChange;
import com.swp391.hivtmss.model.payload.enums.AppointmentChangeStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

@UtilityClass
public class AppointmentChangeSpecification {

    public static Specification<AppointmentChange> doctorNameContaining(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            Join<AppointmentChange, Account> oldDoctorJoin = root.join("oldDoctor"); // Tên biến trong entity Appointment (kiểu Account)
            Join<AppointmentChange, Account> newDoctorJoin = root.join("newDoctor");

            Predicate oldDoctorPredicate = cb.or(
                    cb.like(cb.lower(oldDoctorJoin.get("firstName")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(oldDoctorJoin.get("lastName")), "%" + keyword.toLowerCase() + "%")
            );

            Predicate newDoctorPredicate = cb.or(
                    cb.like(cb.lower(newDoctorJoin.get("firstName")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(newDoctorJoin.get("lastName")), "%" + keyword.toLowerCase() + "%")
            );

            return cb.or(oldDoctorPredicate, newDoctorPredicate);
        };
    }

    public static Specification<AppointmentChange> hasStatus(AppointmentChangeStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<AppointmentChange> hasOldDoctorName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            Join<AppointmentChange, Account> accountJoin = root.join("oldDoctor"); // Tên biến trong entity AppointmentChange (kiểu Account)

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(accountJoin.get("firstName")), likePattern),
                    cb.like(cb.lower(accountJoin.get("lastName")), likePattern)
            );
        };
    }

    public static Specification<AppointmentChange> hasNewDoctorName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null;
            }

            Join<AppointmentChange, Account> accountJoin = root.join("newDoctor"); // Tên biến trong entity AppointmentChange (kiểu Account)

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(accountJoin.get("firstName")), likePattern),
                    cb.like(cb.lower(accountJoin.get("lastName")), likePattern)
            );
        };
    }


    public static Specification<AppointmentChange> hasCreatedDateBetween(Date startTime, Date endTime) {
        return (root, query, cb) -> {
            if (startTime == null || endTime == null) {
                return null;
            }
            return cb.between(root.get("createdDate"), startTime, endTime);
        };
    }
}
