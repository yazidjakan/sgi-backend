package com.jakan.notification_service.controller;

import com.jakan.notification_service.dto.NotificationDTO;
import com.jakan.notification_service.entity.model.EmailRequest;
import com.jakan.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<NotificationDTO> sendEmail(@RequestBody EmailRequest request) {
        return ResponseEntity.ok(notificationService.sendEmail(request));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(notificationService.getNotificationsByEmail(email));
    }

    @GetMapping("/email/{email}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @PathVariable String email) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(email));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

}
