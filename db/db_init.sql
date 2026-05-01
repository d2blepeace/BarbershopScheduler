## BARBERSHOP DATABASE INITIALIZATION

DROP DATABASE IF EXISTS barbershop_db;
CREATE DATABASE barbershop_db;
USE barbershop_db;

## TABLES
CREATE TABLE services (
    service_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100)   NOT NULL,
    duration     INT            NOT NULL,
    price        DECIMAL(10,2)  NOT NULL,
    type         VARCHAR(50)    NOT NULL
);

CREATE TABLE providers (
    provider_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    bio          VARCHAR(500),
    avatar_url   VARCHAR(500),
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE availability_slots (
    slot_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id  BIGINT  NOT NULL,
    date         DATE    NOT NULL,
    time         TIME    NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE appointments (
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id    BIGINT      NOT NULL,
    provider_id    BIGINT      NOT NULL,
    service_id     BIGINT      NOT NULL,
    slot_id        BIGINT      NOT NULL,
    status         VARCHAR(20) NOT NULL,
    booked_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    notes          VARCHAR(500)
);

## Services offered by the salon
INSERT INTO services (service_name, duration, price, type) VALUES
('Hair cut',                   30,  35.00, 'Hair'),
('Women hair cut & Styling',   55,  50.00, 'Hair'),
('Men hair cut & Shaving',     45,  50.00, 'Hair'),
('Women hair cut & Color',    120, 150.00, 'Hair'),
('Men hair cut & Beard trim',  60,  70.00, 'Hair'),
('Quick nail service',         30,  50.00, 'Nail'),
('Kid hair cut',               30,  20.00, 'Hair'),
('Nail manicure & Coloring',   60, 100.00, 'Nail');

## Providers (Verso & Sciel are inactive => dimmed in UI)
INSERT INTO providers (name, bio, avatar_url, is_active) VALUES
('Lune',    'Barber',          NULL, TRUE),
('Gustave', 'Barber',          NULL, TRUE),
('Maelle',  'Nail Technician', NULL, TRUE),
('Monoco',  'Nail Technician', NULL, TRUE),
('Verso',   'Barber',          NULL, FALSE),
('Sciel',   'Barber',          NULL, FALSE);

## Availability slots: next 14 days, 7 time slots per day, for active providers
INSERT INTO availability_slots (provider_id, date, time, is_available)
SELECT p.provider_id, dates.d, times.t, TRUE
FROM providers p
JOIN (
    SELECT CURDATE() + INTERVAL 1 DAY  AS d UNION SELECT CURDATE() + INTERVAL 2 DAY
    UNION SELECT CURDATE() + INTERVAL 3 DAY  UNION SELECT CURDATE() + INTERVAL 4 DAY
    UNION SELECT CURDATE() + INTERVAL 5 DAY  UNION SELECT CURDATE() + INTERVAL 6 DAY
    UNION SELECT CURDATE() + INTERVAL 7 DAY  UNION SELECT CURDATE() + INTERVAL 8 DAY
    UNION SELECT CURDATE() + INTERVAL 9 DAY  UNION SELECT CURDATE() + INTERVAL 10 DAY
    UNION SELECT CURDATE() + INTERVAL 11 DAY UNION SELECT CURDATE() + INTERVAL 12 DAY
    UNION SELECT CURDATE() + INTERVAL 13 DAY UNION SELECT CURDATE() + INTERVAL 14 DAY
) dates
JOIN (
    SELECT '09:00:00' AS t UNION SELECT '10:00:00' UNION SELECT '11:00:00'
    UNION SELECT '11:30:00' UNION SELECT '15:00:00' UNION SELECT '16:30:00'
    UNION SELECT '18:00:00'
) times
WHERE p.is_active = TRUE;

## VERIFICATION QUERIES (optional - for sanity checking)
SELECT * FROM services;
SELECT * FROM providers;
SELECT COUNT(*) AS total_slots FROM availability_slots;