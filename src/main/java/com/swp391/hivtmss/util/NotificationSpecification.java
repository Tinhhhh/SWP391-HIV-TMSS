package com.swp391.hivtmss.util;

import com.swp391.hivtmss.model.entity.Notification;
import com.swp391.hivtmss.model.payload.enums.NotificationStatus;
import com.swp391.hivtmss.model.payload.enums.NotificationStatusFilter;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@UtilityClass
public class NotificationSpecification {

    public static Specification<Notification> hasStatus(NotificationStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Notification> hasId(UUID accountId) {
        return (root, query, cb) -> {
            if (accountId == null) {
                return null;
            }
            return cb.equal(root.get("account").get("id"), accountId);
        };
    }


}
