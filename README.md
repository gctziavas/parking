# SpaceDrop 🅿️

**SpaceDrop** is a smart parking platform that helps users find and book parking spots in their city. It features separate apps for **users**, **parking lot owners**, and **platform administrators**.

## Architecture

The platform is built on a microservices architecture using **Spring Boot** (Java 17) for the backend, **Apache Kafka** (KRaft mode, no Zookeeper) for inter-service communication, and **React Native** (Expo) for mobile apps.

```
┌─────────────────────────────────────────────────────────────┐
│                     Mobile Apps (React Native / Expo)        │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────────┐    │
│  │ SpaceDrop   │  │ SpaceDrop    │  │ SpaceDrop       │    │
│  │ User App    │  │ Owner App    │  │ Admin App       │    │
│  └──────┬──────┘  └──────┬───────┘  └────────┬────────┘    │
└─────────┼────────────────┼───────────────────┼──────────────┘
          │                │                   │
          └────────────────┼───────────────────┘
                           │
                    ┌──────▼──────┐
                    │ API Gateway │ :8080
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
       ┌──────▼──┐  ┌──────▼───┐  ┌────▼─────┐
       │  User   │  │ Parking  │  │ Booking  │
       │ Service │  │ Service  │  │ Service  │
       │  :8081  │  │  :8082   │  │  :8083   │
       └────┬────┘  └────┬─────┘  └────┬─────┘
            │             │             │
            └─────────────┼─────────────┘
                          │
                   ┌──────▼──────┐
                   │    Kafka    │
                   │ (KRaft mode)│
                   └──────┬──────┘
                          │
                ┌─────────▼─────────┐
                │  Notification     │
                │  Service :8084    │
                └───────────────────┘
```

## Backend Services

| Service | Port | Description |
|---------|------|-------------|
| **Discovery Service** | 8761 | Eureka service registry for service discovery |
| **API Gateway** | 8080 | Spring Cloud Gateway — routes requests to microservices |
| **User Service** | 8081 | User registration, authentication, and profile management |
| **Parking Service** | 8082 | Parking lot CRUD, availability, and location-based search |
| **Booking Service** | 8083 | Parking spot reservations and booking lifecycle |
| **Notification Service** | 8084 | Consumes Kafka events and sends notifications |

### Kafka Topics

- `user-events` — User registration and profile updates
- `parking-events` — Parking lot creation and availability changes
- `booking-events` — Booking lifecycle events (created, confirmed, cancelled, completed)

## Mobile Apps

| App | Directory | Target Users |
|-----|-----------|-------------|
| **SpaceDrop** | `mobile/spacedrop-user` | End users searching for and booking parking |
| **SpaceDrop Owner** | `mobile/spacedrop-owner` | Parking lot owners managing their lots |
| **SpaceDrop Admin** | `mobile/spacedrop-admin` | Platform administrators |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Cloud 2023.0
- **Messaging**: Apache Kafka (KRaft mode — no Zookeeper)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Database**: PostgreSQL (production), H2 (development)
- **Mobile**: React Native with Expo
- **Infrastructure**: Docker Compose

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose

### Start Infrastructure

```bash
docker compose up -d
```

This starts Kafka (KRaft mode) and PostgreSQL databases.

### Build & Run Backend

```bash
cd backend
mvn clean install

# Start services in order:
# 1. Discovery Service
cd discovery-service && mvn spring-boot:run &

# 2. API Gateway
cd ../api-gateway && mvn spring-boot:run &

# 3. Microservices
cd ../user-service && mvn spring-boot:run &
cd ../parking-service && mvn spring-boot:run &
cd ../booking-service && mvn spring-boot:run &
cd ../notification-service && mvn spring-boot:run &
```

### Run Mobile Apps

```bash
# User App
cd mobile/spacedrop-user
npm install
npx expo start

# Owner App
cd mobile/spacedrop-owner
npm install
npx expo start

# Admin App
cd mobile/spacedrop-admin
npm install
npx expo start
```

## API Endpoints

### User Service (`/api/users`)
- `POST /api/users` — Create a new user
- `GET /api/users` — List all users
- `GET /api/users/{id}` — Get user by ID
- `DELETE /api/users/{id}` — Delete a user

### Parking Service (`/api/parking`)
- `POST /api/parking` — Create a parking lot
- `GET /api/parking` — List active parking lots
- `GET /api/parking/available` — List parking lots with availability
- `GET /api/parking/{id}` — Get parking lot by ID
- `GET /api/parking/owner/{ownerId}` — Get parking lots by owner
- `PUT /api/parking/{id}` — Update a parking lot
- `DELETE /api/parking/{id}` — Delete a parking lot

### Booking Service (`/api/bookings`)
- `POST /api/bookings` — Create a booking
- `GET /api/bookings/{id}` — Get booking by ID
- `GET /api/bookings/user/{userId}` — Get bookings by user
- `GET /api/bookings/parking/{parkingLotId}` — Get bookings by parking lot
- `PUT /api/bookings/{id}/confirm` — Confirm a booking
- `PUT /api/bookings/{id}/cancel` — Cancel a booking
- `PUT /api/bookings/{id}/complete` — Complete a booking