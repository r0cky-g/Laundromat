CREATE TABLE notifications (
    notification_id BIGINT NOT NULL AUTO_INCREMENT,
    account_id VARCHAR(255) NOT NULL,
    email VARCHAR(254) NOT NULL,
    type VARCHAR(64) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT,
    status VARCHAR(32) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at DATETIME,
    PRIMARY KEY (notification_id),
    INDEX idx_notifications_account_created (account_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
