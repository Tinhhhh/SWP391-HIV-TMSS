package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.enums.NotificationStatusFilter;
import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Object> getAllNotifications(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam(value = "status", required = false) NotificationStatusFilter status,
            @RequestParam(value = "accountId") UUID accountId
    ) {
        return ResponseBuilder.returnData(HttpStatus.OK, "Get all notifications successfully",
                notificationService.getAllNotification(pageNo, pageSize, sortBy, sortDir, accountId, status));

    }

    @PutMapping("/all")
    public ResponseEntity<Object> readAllNotifications(
            @RequestParam(value = "accountId") UUID accountId,
            @RequestParam List<Long> notificationIds
    ) {
        notificationService.readAllNotifications(notificationIds, accountId);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Read all notifications successfully");
    }

    @PutMapping
    public ResponseEntity<Object> readNotification(
            @RequestParam(value = "accountId") UUID accountId,
            @RequestParam Long notificationId
    ) {
        notificationService.readNotification(notificationId, accountId);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Read notification successfully");
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteNotification(
            @RequestParam(value = "accountId") UUID accountId,
            @RequestParam Long notificationId
    ) {
        notificationService.deleteNotification(notificationId, accountId);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete notification successfully");
    }
}
