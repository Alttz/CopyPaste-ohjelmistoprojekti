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
    date VARCHAR(255) NOT NULL,
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


INSERT INTO event (date, place, city, name, ticket_count) VALUES
('2023-09-28', 'Hartwallareena', 'Helsinki', 'Lordi', 1000),
('2024-04-01', 'PubiTarmo', 'Turku', 'Apulanta', 1000),
('2024-07-18', 'Kansallisteatteri', 'Helsinki', 'Käärijä', 1000),
('2024-05-05', 'Koulun musaluokka', 'Luhanka', 'Antti Tuisku', 1000);

INSERT INTO ticket_types (event_id, name, price) VALUES
(1, 'Eläkeläinen', 15.00),
(1, 'Opiskelija', 15.00),
(1, 'Aikuinen', 25.00),
(2, 'Eläkeläinen', 15.00),
(2, 'Opiskelija', 15.00),
(2, 'Aikuinen', 25.00),
(3, 'Eläkeläinen', 40.00),
(3, 'Opiskelija', 40.00),
(3, 'Aikuinen', 60.00),
(4, 'Eläkeläinen', 20.00),
(4, 'Opiskelija', 20.00),
(4, 'Aikuinen', 35.00);

INSERT INTO app_user (username, password, role) VALUES
('TeppoTestaaja', '$2a$10$zhpiBrz5wSxgEdwS3NhywuLF1WLA9AG4bUF19NPHVWuxeZgW9jxQ2', 'ROLE_ADMIN'),
('Masa', '$2a$10$cJuQ/74HLrnvXNgjzgqm7.GRKRbsDMnnofNcSH0CQa1B6Hn9iehXq', 'ROLE_ADMIN'),
('admin', '$2a$10$B1iQXs9Wu.zMgDQqnQeY.utIZF5vRnJRXtP0WuvvOMMrSqnmFSvWi', 'ROLE_ADMIN'),
('user', '$2a$10$vKueiYJYRWr2vjp38z4uUu0KAOk/McpSO4TvAk9rRTdJ5XjNNCdfi', 'ROLE_USER');

INSERT INTO purchase (appuser_id, purchase_date) VALUES
(1, '2024-04-08'),
(2, '2024-04-08');

INSERT INTO tickets (event_id, ticket_type_id, purchase_id, is_used) VALUES
(1, 1, 1, FALSE),
(1, 2, 1, FALSE),
(1, 3, 2, FALSE),
(1, 3, 2, FALSE);
