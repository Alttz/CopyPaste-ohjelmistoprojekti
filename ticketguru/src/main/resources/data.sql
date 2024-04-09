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