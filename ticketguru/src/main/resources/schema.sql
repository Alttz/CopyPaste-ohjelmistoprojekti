-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS ticket_types;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS purchase;
DROP TABLE IF EXISTS tickets;

CREATE TABLE IF NOT EXISTS event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
    name VARCHAR(255) NOT NULL,
    place VARCHAR(255) NOT NULL,
    ticket_count INT NOT NULL
);

CREATE TABLE IF NOT EXISTS ticket_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id)
);

CREATE TABLE IF NOT EXISTS app_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appuser_id BIGINT NOT NULL,
    purchase_date DATETIME NOT NULL,
    FOREIGN KEY (appuser_id) REFERENCES app_user(user_id)
);

CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type_id BIGINT NOT NULL,
    purchase_id BIGINT,
    is_used BOOLEAN NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id),
    FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id),
    FOREIGN KEY (purchase_id) REFERENCES purchase(id)
);
