## BARBERSHOP DATABASE INITIALIZATION
## Run with: mysql -u root -p < db_init.sql


DROP DATABASE IF EXISTS barbershop_db;
CREATE DATABASE barbershop_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE barbershop_db;

## TABLES
CREATE TABLE services (
    service_id   BIGINT       PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    duration     INT          NOT NULL,
    price        DECIMAL(10,2) NOT NULL,
    type         VARCHAR(50)  NOT NULL
) ENGINE=InnoDB;

CREATE TABLE providers (
    provider_id  BIGINT       PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    bio          VARCHAR(500),
    avatar_url   VARCHAR(500),
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE availability_slots (
    slot_id      BIGINT  PRIMARY KEY AUTO_INCREMENT,
    provider_id  BIGINT  NOT NULL,
    date         DATE    NOT NULL,
    time         TIME    NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_slot_provider
        FOREIGN KEY (provider_id) REFERENCES providers(provider_id),
    INDEX idx_slot_provider_date (provider_id, date, is_available)
) ENGINE=InnoDB;

CREATE TABLE appointments (
    appointment_id   BIGINT      PRIMARY KEY AUTO_INCREMENT,
    customer_id      BIGINT      NOT NULL,
    provider_id      BIGINT      NOT NULL,
    service_id       BIGINT      NOT NULL,
    slot_id          BIGINT      NOT NULL,
    appointment_date DATE        NOT NULL,
    appointment_time TIME        NOT NULL,
    status           VARCHAR(20) NOT NULL,
    booked_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes            VARCHAR(500),
    CONSTRAINT fk_appt_provider
        FOREIGN KEY (provider_id) REFERENCES providers(provider_id),
    CONSTRAINT fk_appt_service
        FOREIGN KEY (service_id)  REFERENCES services(service_id),
    CONSTRAINT fk_appt_slot
        FOREIGN KEY (slot_id)     REFERENCES availability_slots(slot_id),
    CONSTRAINT chk_appt_status
        CHECK (status IN ('CONFIRMED', 'CANCELLED', 'COMPLETED')),
    INDEX idx_appt_customer (customer_id),
    INDEX idx_appt_slot (slot_id)
) ENGINE=InnoDB;

##  SEED DATA

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

## Providers (Verso & Sciel are inactive -> dimmed in UI)
INSERT INTO providers (name, bio, avatar_url, is_active) VALUES
('Lune',    'Barber',          NULL, TRUE),
('Gustave', 'Barber',          NULL, TRUE),
('Maelle',  'Nail Technician', NULL, TRUE),
('Monoco',  'Nail Technician', NULL, TRUE),
('Verso',   'Barber',          NULL, FALSE),
('Sciel',   'Barber',          NULL, FALSE);

## Availability slots: next 14 days, 7 time slots per day, active providers only
WITH RECURSIVE date_range AS (
    SELECT CURDATE() + INTERVAL 1 DAY AS d
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM date_range WHERE d < CURDATE() + INTERVAL 14 DAY
)
INSERT INTO availability_slots (provider_id, date, time, is_available)
SELECT p.provider_id, dr.d, t.time_slot, TRUE
FROM providers p
CROSS JOIN date_range dr
CROSS JOIN (
    SELECT '09:00:00' AS time_slot UNION ALL
    SELECT '10:00:00' UNION ALL
    SELECT '11:00:00' UNION ALL
    SELECT '11:30:00' UNION ALL
    SELECT '15:00:00' UNION ALL
    SELECT '16:30:00' UNION ALL
    SELECT '18:00:00'
) t
WHERE p.is_active = TRUE;

## verification (optional comment out for silent runs)
SELECT COUNT(*) AS total_services FROM services;
SELECT COUNT(*) AS total_providers FROM providers;
SELECT COUNT(*) AS total_slots FROM availability_slots;