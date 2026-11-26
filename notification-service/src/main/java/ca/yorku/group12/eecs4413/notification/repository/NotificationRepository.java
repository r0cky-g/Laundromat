package ca.yorku.group12.eecs4413.notification.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import ca.yorku.group12.eecs4413.notification.model.Notification;

@Repository
public class NotificationRepository {

    private static final String INSERT_NOTIFICATION = """
            INSERT INTO notifications (account_id, email, type, subject, message, status, created_at, sent_at)
            VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP), ?)
            """;

    private static final String SELECT_ACCOUNT_NOTIFICATIONS = """
            SELECT notification_id, account_id, email, type, subject, message, status, created_at, sent_at
            FROM notifications
            WHERE account_id = ?
            ORDER BY created_at DESC, notification_id DESC
            """;

    private static final String SELECT_ACCOUNT_NOTIFICATION = """
            SELECT notification_id, account_id, email, type, subject, message, status, created_at, sent_at
            FROM notifications
            WHERE notification_id = ? AND account_id = ?
            """;

    private static final String UPDATE_DELIVERY_STATUS = """
            UPDATE notifications
            SET status = ?, sent_at = ?
            WHERE notification_id = ?
            """;

    private final DataSource dataSource;

    public NotificationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long insert(Notification notification) throws SQLException {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(INSERT_NOTIFICATION, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, notification.accountId());
            statement.setString(2, notification.email());
            statement.setString(3, notification.type());
            statement.setString(4, notification.subject());
            statement.setString(5, notification.message());
            statement.setString(6, notification.status());
            statement.setObject(7, notification.createdAt());
            statement.setObject(8, notification.sentAt());
            statement.executeUpdate();

            try (var keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("No ID was returned for the new notification.");
                }
                return keys.getLong(1);
            }
        }
    }

    public List<Notification> findByAccountId(String accountId) throws SQLException {
        var notifications = new ArrayList<Notification>();
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_ACCOUNT_NOTIFICATIONS)) {
            statement.setString(1, accountId);
            try (var rows = statement.executeQuery()) {
                while (rows.next()) {
                    notifications.add(readNotification(rows));
                }
            }
        }
        return notifications;
    }

    public Optional<Notification> findByIdAndAccountId(long notificationId, String accountId)
            throws SQLException {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_ACCOUNT_NOTIFICATION)) {
            statement.setLong(1, notificationId);
            statement.setString(2, accountId);
            try (var rows = statement.executeQuery()) {
                if (rows.next()) {
                    return Optional.of(readNotification(rows));
                }
                return Optional.empty();
            }
        }
    }

    public boolean updateDeliveryStatus(long notificationId, String status, LocalDateTime sentAt)
            throws SQLException {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(UPDATE_DELIVERY_STATUS)) {
            statement.setString(1, status);
            statement.setObject(2, sentAt);
            statement.setLong(3, notificationId);
            return statement.executeUpdate() == 1;
        }
    }

    private Notification readNotification(ResultSet rows) throws SQLException {
        return new Notification(
                rows.getLong("notification_id"),
                rows.getString("account_id"),
                rows.getString("email"),
                rows.getString("type"),
                rows.getString("subject"),
                rows.getString("message"),
                rows.getString("status"),
                rows.getObject("created_at", LocalDateTime.class),
                rows.getObject("sent_at", LocalDateTime.class));
    }
}
