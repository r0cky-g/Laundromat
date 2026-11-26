package ca.yorku.group12.eecs4413.notification.dto;

import java.time.LocalDateTime;

public record NotificationSummary(
        long notificationId,
        String type,
        String subject,
        String status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
}
