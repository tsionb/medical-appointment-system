# Medical Time Scheduling and Patient Appointment Management System

A web-based application for managing patient appointments in healthcare facilities. Built with Spring Boot multi-module architecture.

---

## Tech Stack

| Component       | Technology                    |
|----------------|-------------------------------|
| Backend         | Spring Boot 3.2.5             |
| Database        | PostgreSQL 15                 |
| ORM             | Spring Data JPA / Hibernate   |
| Security        | Spring Security + JWT         |
| Email           | JavaMail + Mailtrap           |
| Containerization| Docker + Docker Compose       |
| Build Tool      | Maven (multi-module)          |

---

## Module Structure

appointment-system/
├── common/ # Shared exceptions, enums, DTOs
├── department-module/ # Departments and operating hours
├── doctor-module/ # Doctors, schedules, reviews
├── patient-module/ # Patient profiles
├── appointment-module/ # Bookings and waitlist
├── medical-records-module/ # Medical records and prescriptions
├── notification-module/ # Email notifications
└── security-module/ # JWT auth — boots the application


---

## Features

- Department and operating hours management
- Doctor profiles with schedule slot management
- Patient registration and profile management
- Appointment booking with real-time slot availability
- Operating hours validation on booking
- Waitlist with automatic promotion on cancellation
- Medical records and prescription management
- Patient reviews and doctor ratings
- Email notifications (booking, reminder, cancellation, promotion)
- Daily appointment reminder scheduler
- JWT-based authentication
- Role-based access control (ADMIN, DOCTOR, PATIENT)
- Global exception handling with consistent error responses
- Bean validation on all request DTOs

---

## Prerequisites

- Docker Desktop (running)
- Java 17+ (for local development)
- Maven 3.9+ (for local development)

---

## Running with Docker (Recommended)

### 1. Clone the repository
```bash
git clone https://github.com/your-username/medical-appointment-system.git
cd medical-appointment-system
```

### 2. Create .env file
```bash
# Create .env in project root
MAILTRAP_USERNAME=your_mailtrap_username
MAILTRAP_PASSWORD=your_mailtrap_password
```

### 3. Start everything
```bash
docker-compose up --build
```

The application starts at `http://localhost:8080`
pgAdmin starts at `http://localhost:5050`

### 4. Stop everything
```bash
docker-compose down
```

### 5. Stop and remove all data
```bash
docker-compose down -v
```

---

## Running Locally (Development)

### 1. Start only the database
```bash
docker-compose up postgres -d
```

### 2. Run the Spring Boot app
```bash
cd security-module
mvn spring-boot:run
```

---

## API Endpoints

### Authentication (Public)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Departments (ADMIN write, Public read)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/departments | Get all departments |
| GET | /api/departments/{id} | Get department by ID |
| POST | /api/departments | Create department |
| PUT | /api/departments/{id} | Update department |
| DELETE | /api/departments/{id} | Delete department |
| POST | /api/departments/{id}/schedules | Add operating hours |
| GET | /api/departments/{id}/schedules | Get operating hours |
| DELETE | /api/departments/{id}/schedules/{sid} | Remove operating hours |

### Doctors (ADMIN write, Public read)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/doctors | Get all doctors |
| GET | /api/doctors/{id} | Get doctor by ID |
| POST | /api/doctors | Create doctor |
| PUT | /api/doctors/{id} | Update doctor |
| DELETE | /api/doctors/{id} | Delete doctor |
| POST | /api/doctors/{id}/schedules | Create schedule slot |
| GET | /api/doctors/{id}/schedules | Get all slots |
| GET | /api/doctors/{id}/schedules/available | Get available slots |

### Patients (ADMIN only)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/patients | Get all patients |
| GET | /api/patients/{id} | Get patient by ID |
| PUT | /api/patients/{id} | Update patient |

### Appointments (PATIENT book, DOCTOR/ADMIN manage)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/appointments | Book appointment |
| GET | /api/appointments/{id} | Get appointment |
| GET | /api/appointments/patient/{id} | Get patient appointments |
| GET | /api/appointments/doctor/{id} | Get doctor appointments |
| PATCH | /api/appointments/{id}/status | Update status |
| DELETE | /api/appointments/{id}/cancel | Cancel appointment |

### Waitlist (PATIENT)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/waitlist | Join waitlist |
| GET | /api/waitlist/patient/{id} | Get patient waitlist |
| GET | /api/waitlist/{id}/position | Get queue position |
| DELETE | /api/waitlist/{id}/cancel | Cancel waitlist entry |

### Medical Records (DOCTOR create, PATIENT read own)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/medical-records | Create medical record |
| GET | /api/medical-records/{id} | Get record |
| GET | /api/medical-records/patient/{id} | Get patient records |
| PUT | /api/medical-records/{id} | Update record |
| POST | /api/medical-records/{id}/prescriptions | Add prescription |

### Medications (ADMIN write, Authenticated read)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/medications | Get all medications |
| GET | /api/medications/search?name= | Search by name |
| POST | /api/medications | Add medication |
| PUT | /api/medications/{id} | Update medication |

### Reviews (PATIENT create, Public read)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/appointments/{id}/reviews | Submit review |
| GET | /api/doctors/{id}/reviews | Get doctor reviews |

### Notifications (ADMIN only)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/notifications/patient/{id} | Patient notifications |
| GET | /api/notifications/appointment/{id} | Appointment notifications |
| GET | /api/notifications/failed | Failed notifications |

---

## Authentication

All protected endpoints require a JWT token in the Authorization header: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

Get a token by calling `/api/auth/login`.

### Roles and Access

| Role | Access |
|------|--------|
| ADMIN | Full system access — manages departments, doctors, medications |
| DOCTOR | Manages own schedules, creates medical records and prescriptions |
| PATIENT | Books appointments, joins waitlist, submits reviews |

---

## Database

PostgreSQL 15 running in Docker.

**pgAdmin:** `http://localhost:5050`
- Email: `admin@medical.com`
- Password: `admin123`
- Connect to: Host `postgres`, Port `5432`, DB `medical_db`

**Tables:** departments, department_schedules, doctors, schedules, reviews,
patients, appointments, waitlist, medical_records, medications,
prescriptions, notification_logs, users

---

## Email Testing

Uses Mailtrap for email sandbox testing.
All emails go to your Mailtrap inbox — no real emails are sent.


---

