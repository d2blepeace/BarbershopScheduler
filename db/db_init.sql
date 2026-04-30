CREATE DATABASE barbershop_db;
USE barbershop_db;
ALTER TABLE appointments DROP INDEX uq_slot;

CREATE TABLE services (
    service_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE availability_slots (
    slot_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE appointments (
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes VARCHAR(500),
);
DELETE FROM services;

INSERT INTO services (service_name, duration, price, type) VALUES
('Hair cut',                   30,  35.00, 'Hair'),
('Women hair cut & Styling',   55,  50.00, 'Hair'),
('Men hair cut & Shaving',     45,  50.00, 'Hair'),
('Women hair cut & Color',    120, 150.00, 'Hair'),
('Men hair cut & Beard trim',  60,  70.00, 'Hair'),
('Quick nail service',         30,  50.00, 'Nail'),
('Kid hair cut',               30,  20.00, 'Hair'),
('Nail manicure & Coloring',   60, 100.00, 'Nail');

INSERT INTO availability_slots (provider_id, date, time, is_available)
VALUES
(1, '2026-03-30', '09:00:00', TRUE),
(1, '2026-03-30', '10:00:00', TRUE),
(1, '2026-03-30', '11:00:00', TRUE);

SHOW DATABASES;
USE barbershop_db;
SHOW TABLES;
SELECT * FROM services;
SELECT * FROM availability_slots;
