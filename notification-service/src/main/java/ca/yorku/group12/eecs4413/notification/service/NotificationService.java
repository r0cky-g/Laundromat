package ca.yorku.group12.eecs4413.notification.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ca.yorku.group12.eecs4413.notification.dto.NotificationDetail;
import ca.yorku.group12.eecs4413.notification.dto.NotificationSummary;
import ca.yorku.group12.eecs4413.notification.model.Notification;
import ca.yorku.group12.eecs4413.notification.repository.NotificationRepository;

@Service
public class NotificationService {

    private static final DateTimeFormatter RESERVATION_TIME = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a O",
            Locale.ENGLISH);

    private static final String RESERVATION_CONFIRMED = "RESERVATION_CONFIRMED";

    private static final String RESERVATION_CANCELLED = "RESERVATION_CANCELLED";
    private static final String MACHINE_CYCLE_COMPLETED = "MACHINE_CYCLE_COMPLETED";

    private static final String PASSWORD_RESET = "PASSWORD_RESET";

    // Hide password reset verification codes from notification history.
    private static final Set<String> TYPES_WITH_BODY_IN_HISTORY = Set.of(
            RESERVATION_CONFIRMED, RESERVATION_CANCELLED, MACHINE_CYCLE_COMPLETED);

    private final NotificationRepository repository;

    private final ObjectProvider<JavaMailSender> mailSenders;
    private final String mailFrom;

    public NotificationService(NotificationRepository repository, ObjectProvider<JavaMailSender> mailSenders,
            @Value("${notification.mail.from}") String mailFrom) {
        this.repository = repository;
        this.mailSenders = mailSenders;
        this.mailFrom = mailFrom;
    }

    public long sendReservationConfirmation(String accountId, String email, int machineId,
            OffsetDateTime startTime, OffsetDateTime endTime) throws SQLException {
        return sendEmail(accountId, email, RESERVATION_CONFIRMED, "Reservation confirmed",
                reservationConfirmation(machineId, startTime, endTime));
    }

    public long sendReservationCancellation(String accountId, String email, int machineId,
            OffsetDateTime startTime, OffsetDateTime endTime) throws SQLException {
        String body = "Your reservation for machine #%d from %s to %s has been cancelled."
                .formatted(machineId, startTime.format(RESERVATION_TIME), endTime.format(RESERVATION_TIME));
        return sendEmail(accountId, email, RESERVATION_CANCELLED, "Reservation cancelled", body);
    }

    public long sendCycleCompletion(String accountId, String email, int machineId) throws SQLException {
        return sendEmail(accountId, email, MACHINE_CYCLE_COMPLETED, "Laundry cycle complete",
                cycleCompletion(machineId));
    }

    public long sendPasswordResetCode(String accountId, String email, String verificationCode) throws SQLException {
        if (verificationCode == null || !verificationCode.matches("[0-9]{6}")) {
            throw new IllegalArgumentException("The reset code must contain six digits.");
        }
        String body = "Your Absolute Laundry password reset code is %s. "
                .formatted(verificationCode) + "If you did not request a password reset, you can ignore this email.";
        return sendEmail(accountId, email, PASSWORD_RESET, "Password reset code", body);
    }

    private long sendEmail(String accountId, String email, String type, String subject, String body)
            throws SQLException {
        if (mailFrom.isBlank()) {
            throw new IllegalStateException("Set MAIL_FROM before sending notifications.");
        }
        JavaMailSender mailSender = mailSenders.getObject();

        var message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        boolean passwordReset = PASSWORD_RESET.equals(type);
        String storedBody = passwordReset ? null : body;
        long notificationId = repository.insert(new Notification(
                null, accountId, email, type, subject, storedBody, "PENDING", null, null));
        try {
            mailSender.send(message);
        } catch (MailException sendFailure) {
            // The original mail error may include the reset code.
            MailException failure = passwordReset
                    ? new MailSendException("Password reset email failed.")
                    : sendFailure;
            try {
                recordDeliveryStatus(notificationId, "FAILED", null);
            } catch (SQLException updateFailure) {
                failure.addSuppressed(updateFailure);
            }
            throw failure;
        }

        recordDeliveryStatus(notificationId, "SENT", LocalDateTime.now(ZoneOffset.UTC));
        return notificationId;
    }

    private void recordDeliveryStatus(long notificationId, String status, LocalDateTime sentAt)
            throws SQLException {
        if (!repository.updateDeliveryStatus(notificationId, status, sentAt)) {
            throw new SQLException("Could not update delivery status for notification " + notificationId);
        }
    }

    public List<NotificationSummary> getHistory(String accountId) throws SQLException {
        return repository.findByAccountId(accountId).stream()
                .map(notification -> new NotificationSummary(
                        notification.notificationId(), notification.type(), notification.subject(),
                        notification.status(), notification.createdAt(), notification.sentAt()))
                .toList();
    }

    public Optional<NotificationDetail> getDetail(long notificationId, String accountId) throws SQLException {
        return repository.findByIdAndAccountId(notificationId, accountId)
                .map(notification -> new NotificationDetail(
                        notification.notificationId(), notification.type(), notification.subject(),
                        notification.status(), notification.createdAt(), notification.sentAt(),
                        TYPES_WITH_BODY_IN_HISTORY.contains(notification.type()) ? notification.message() : null));
    }

    public String reservationConfirmation(int machineId, OffsetDateTime startTime, OffsetDateTime endTime) {
        return "Your reservation for machine #%d is confirmed from %s to %s."
                .formatted(machineId, startTime.format(RESERVATION_TIME), endTime.format(RESERVATION_TIME));
    }

    public String cycleCompletion(int machineId) {
        return "Machine #%d has finished its cycle. Your laundry is ready to pick up."
                .formatted(machineId);
    }
}
