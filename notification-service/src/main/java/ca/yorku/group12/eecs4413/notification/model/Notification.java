package ca.yorku.group12.eecs4413.notification.model;

import java.time.LocalDateTime;

public record Notification(
        Long notificationId,
        String accountId,
        String email,
        String type,
        String subject,
        String message,
        String status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
}
