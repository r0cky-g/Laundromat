package ca.yorku.group12.eecs4413.notification.dto;

import java.time.LocalDateTime;

public record NotificationDetail(
        long notificationId,
        String type,
        String subject,
        String status,
        LocalDateTime createdAt,
        LocalDateTime sentAt,
        String message
) {
}
