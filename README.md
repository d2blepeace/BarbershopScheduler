# Vintage Razor Barbershop & Salon — Online Appointment Scheduling System

A small enterprise-style backend for booking, canceling, and managing barber and salon appointments. Built for **CMPE 172 — Spring 2026**.

---

## Features

- Browse available time slots by provider and date
- Book, cancel, and view appointments
- Provider-side availability management
- Double-booking prevention via pessimistic row locking + conditional updates
- Mock external notification service as a coarse-grained distribution boundary
- File-based logging with structured event messages
- Booking metrics and health check endpoint

---

## Tech Stack
- Language: Java 17+ 
- Framework:  Spring Boot 4.0.3 
- Persistence: Spring JDBC (no ORM) 
- Database:  MySQL 8.0+ 
- Build Tool:  Maven (via wrapper) 
- HTTP Client: Spring `RestTemplate` 
- Logging: SLF4J + Logback 
-  Frontend (optional):  React 

> Per project requirements, no ORM (Hibernate/JPA) is used. All persistence goes through hand-written SQL via `JdbcTemplate`.

---

## Project Structure

```
barbershop/
├── db/
│   └── db_init.sql                    # One-shot DB setup script
├── logs/                              # Runtime log output (gitignored)
│   └── .gitkeep
├── frontend/                          # Optional React UI
├── src/main/java/edu/sjsu/cmpe172/barbershop/
│   ├── BarbershopApplication.java
│   ├── config/
│   │   └── AppConfig.java
│   ├── controller/
│   │   ├── AppointmentController.java
│   │   ├── AvailabilityController.java
│   │   ├── HealthController.java
│   │   └── MockNotificationController.java
│   ├── dto/
│   │   ├── AppointmentRequest.java
│   │   ├── NotificationRequest.java
│   │   └── NotificationResponse.java
│   ├── exception/
│   │   └── SlotUnavailableException.java
│   ├── model/
│   │   ├── Appointment.java
│   │   ├── AvailabilitySlot.java
│   │   ├── Provider.java
│   │   └── Service.java
│   ├── repository/
│   │   ├── AppointmentRepository.java
│   │   ├── AvailabilitySlotRepository.java
│   │   └── ServiceRepository.java
│   └── service/
│       ├── AppointmentService.java
│       ├── BookingMetricsService.java
│       ├── NotificationClient.java
│       └── SalonService.java
├── src/main/resources/
│   └── application.properties
├── test-api.http
├── pom.xml
├── mvnw, mvnw.cmd
├── .gitignore
└── README.md
```

---

## Prerequisites

**Required (backend):**
- Java 17 or later — verify with `java -version`
- MySQL 8.0 or later running on `localhost:3306`

> Maven is **not** required globally, the project ships with the Maven wrapper (`mvnw` / `mvnw.cmd`).

**Optional (frontend):**
- Node.js and npm — only needed if you want to use the React UI for testing. The backend can be fully tested via `test-api.http` without it.

---

## Setup

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd barbershop
```

### 2. Initialize the database

From the project root, run the SQL setup script with your MySQL root user:

```bash
mysql -u root -p < https://github.com/d2blepeace/BarbershopScheduler.git
```

This will:

- Drop and recreate the `barbershop_db` database
- Create all tables with foreign keys and indexes
- Seed 8 services, 6 providers, and 14 days of availability slots

> The script begins with `DROP DATABASE IF EXISTS barbershop_db`. Any existing data in that database will be wiped.

### 3. Configure database credentials

Credentials are read from environment variables to keep secrets out of source control. The committed `application.properties` references them as placeholders:

```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
```

Set them before running the app (see below).

---

## Running the Application

### Backend 

**IMPORTANT, MUST INPUT DATABASE USERNAME AND PASSWORD BEFORE EACH SESSION**

**Windows (PowerShell):**

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
.\mvnw spring-boot:run
```

**macOS / Linux:**

```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
./mvnw spring-boot:run
```

The server starts on **`http://localhost:8080`**. Logs stream to the terminal and to `logs/barbershop.log`. Stop with `Ctrl+C`.

### Frontend (optional)

If you want to use the React UI:

```bash
cd frontend
npm install
npm start
```

The UI runs on `http://localhost:3000` and expects the backend at `http://localhost:8080`.

---

## API Endpoints

All endpoints accept and return JSON.

- `GET` - `/slots?providerId={id}&date=YYYY-MM-DD` : List available slots for a provider on a date 
- `POST`- `/appointments`: Book a new appointment 
-`DELETE` - `/appointments/{id}`: Cancel an appointment 
- `GET`- `/appointments/{id}`: Retrieve appointment details 
- `POST`- `/notify`: (Internal) Mock external notification endpoint 
- `GET`- `/health`: Health check, will return `{"status":"UP"}` 
- `GET`-`/metrics`: Booking metrics (success count, failure count, avg latency) 

### Example: Book an appointment

**Request**

```http
POST /appointments HTTP/1.1
Content-Type: application/json

{
  "customerId": 1,
  "serviceId": 2,
  "slotId": 52,
  "notes": "First-time visit"
}
```

**Success response**

```json
{
  "appointmentId": 9,
  "customerId": 1,
  "providerId": 4,
  "serviceId": 2,
  "slotId": 52,
  "appointmentDate": "2026-05-09",
  "appointmentTime": "14:00:00",
  "status": "CONFIRMED",
  "bookedAt": "2026-05-02T14:23:45",
  "notes": "First-time visit"
}
```

**Conflict response (slot already booked)**

```
HTTP/1.1 409 Conflict
Slot 52 is already booked.
```

---

## Database Schema

Four tables back the application:

- **`services`**: catalog of offered services (haircut, color, manicure, etc.) with duration and price
- **`providers`**: barbers/technicians; `is_active` flag controls visibility
- **`availability_slots`**: pre-generated time slots per provider per day; `is_available` flips when booked
- **`appointments`**: booking records linking customer, provider, service, and slot

Key design points:

- **No `UNIQUE(slot_id)` on `appointments`** — concurrency is enforced at the slot level via row locking, not at the appointment level. See the Final Report for rationale.
- **Foreign keys** on `appointments.provider_id`, `service_id`, `slot_id` enforce referential integrity.
- **CHECK constraint** on `appointments.status` restricts values to `'CONFIRMED' | 'CANCELLED' | 'COMPLETED'`.
- **Composite index** `(provider_id, date, is_available)` accelerates the "find open slots" query.
- **InnoDB** is required for `SELECT ... FOR UPDATE` row locking and FK enforcement.

The full schema with seed data is in [`db/db_init.sql`](db/db_init.sql).

---

## Testing

### Option 1 — `test-api.http` (recommended)

A complete set of pre-written API requests covering every endpoint and edge case (successful booking, double-booking conflict, cancellation, etc.).

Open `test-api.http` in:

- **VS Code** with the [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) extension, or
- **IntelliJ IDEA** (built-in HTTP client)

Click "Send Request" above each request block.

### Option 2 — React frontend (more visualize)

If you've started the optional frontend (see [Running the Application](#running-the-application)), open `http://localhost:3000` and book / cancel appointments through the UI. Useful for end-to-end sanity checks.

---

## Limitations & Future Work

- **In-memory metrics**: `BookingMetricsService` resets on every restart. Production would integrate with Prometheus or Micrometer for persistent, scrape-friendly metrics.
- **Synchronous notification dispatch**: Failed notifications are logged but not retried. A real system would publish to a message queue and reconcile asynchronously.
- **No authentication / authorization**: All endpoints are open. Production would require Spring Security with JWT or OAuth.
- **Single-node deployment**: The pessimistic locking strategy depends on a single MySQL primary. Multi-region active-active would require optimistic locking with version columns and conflict resolution.
- **No reschedule endpoint**: Currently only book and cancel are supported. A `PATCH /appointments/{id}` that atomically releases the old slot and claims a new one is a planned extension.
