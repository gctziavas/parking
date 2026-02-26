# SpaceDrop 🅿️

**SpaceDrop** is a smart parking platform that helps users find and book parking spots in their city. It features separate apps for **users**, **parking lot owners**, and **platform administrators**, plus a **web UI** for browser access.

## Architecture

The platform is built on a microservices architecture using **Spring Boot** (Java 17) for the backend, **Apache Kafka** (KRaft mode, no Zookeeper) for inter-service communication, **Keycloak** for OAuth2 authentication, **React Native** (Expo) for mobile apps, and **React** (Vite) for the web UI.

```
┌──────────────────────────────────────────────────────────────────────────┐
│          Clients                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────┐  ┌──────────────┐   │
│  │ Web UI       │  │ Mobile User  │  │ Mobile     │  │ Mobile Admin │   │
│  │ (React/Vite) │  │ App          │  │ Owner App  │  │ App          │   │
│  │ :3000        │  │              │  │            │  │              │   │
│  └──────┬───────┘  └──────┬───────┘  └─────┬──────┘  └──────┬───────┘   │
└─────────┼─────────────────┼────────────────┼────────────────┼────────────┘
          │                 │                │                │
          └─────────────────┼────────────────┘                │
                            │       ┌─────────────┐          │
                            │       │  Keycloak   │──────────┘
                            │       │  (OAuth2)   │
                            │       │  :8180      │
                            │       └──────┬──────┘
                            │              │
                     ┌──────▼──────────────▼──┐
                     │ API Gateway (OAuth2 RS) │ :8080
                     └──────┬─────────────────┘
                            │
          ┌─────────────────┼──────────────────┐
          │                 │                  │
   ┌──────▼──┐  ┌──────────▼──┐  ┌────────────▼──┐
   │  User   │  │  Parking    │  │   Booking     │
   │ Service │  │  Service    │  │   Service     │
   │  :8081  │  │  :8082      │  │   :8083       │
   └────┬────┘  └──────┬──────┘  └───────┬───────┘
        │              │                 │
        └──────────────┼─────────────────┘
                       │
                ┌──────▼──────┐
                │    Kafka    │
                │ (KRaft mode)│
                └──────┬──────┘
                       │
          ┌────────────┼────────────┐
          │                         │
┌─────────▼─────────┐   ┌──────────▼──────────┐
│  Notification     │   │  Payment Service    │
│  Service :8084    │   │  (placeholder)      │
└───────────────────┘   │  :8085              │
                        └─────────────────────┘
```

## Backend Services

| Service | Port | Description |
|---------|------|-------------|
| **Discovery Service** | 8761 | Eureka service registry for service discovery |
| **API Gateway** | 8080 | Spring Cloud Gateway — routes requests, OAuth2 resource server |
| **User Service** | 8081 | User registration, authentication, and profile management |
| **Parking Service** | 8082 | Parking lot CRUD, availability, and location-based search |
| **Booking Service** | 8083 | Parking spot reservations and booking lifecycle |
| **Notification Service** | 8084 | Consumes Kafka events and sends notifications |
| **Payment Service** | 8085 | Payment processing placeholder (initiated, completed, refunded) |

### Kafka Topics

- `user-events` — User registration and profile updates
- `parking-events` — Parking lot creation and availability changes
- `booking-events` — Booking lifecycle events (created, confirmed, cancelled, completed)
- `payment-events` — Payment processing events (initiated, completed, failed, refunded)

## Authentication (Keycloak)

SpaceDrop uses **Keycloak** as an OAuth2 / OpenID Connect identity provider. The API Gateway acts as an OAuth2 Resource Server, validating JWT tokens issued by Keycloak.

| Component | URL |
|-----------|-----|
| **Keycloak Admin Console** | http://localhost:8180 |
| **Realm** | `spacedrop` |
| **Web Client** | `spacedrop-web` (public, authorization code flow) |
| **Mobile Client** | `spacedrop-mobile` (public, authorization code flow) |
| **API Client** | `spacedrop-api` (bearer-only) |

### Default Test Users

| Username | Password | Role |
|----------|----------|------|
| `user@spacedrop.com` | `password` | User |
| `owner@spacedrop.com` | `password` | Owner |
| `admin@spacedrop.com` | `password` | Admin |

## Web UI

The web UI is a **React** application built with **Vite**, providing browser-based access to the platform with role-based views:

- **Find Parking** — Search and browse available parking lots
- **My Bookings** — View and manage your bookings
- **Owner Dashboard** — Manage your parking lots (for owners)
- **Admin Panel** — Platform administration (for admins)

The web UI integrates with Keycloak for authentication and proxies API requests to the gateway.

## Mobile Apps

| App | Directory | Target Users |
|-----|-----------|-------------|
| **SpaceDrop** | `mobile/spacedrop-user` | End users searching for and booking parking |
| **SpaceDrop Owner** | `mobile/spacedrop-owner` | Parking lot owners managing their lots |
| **SpaceDrop Admin** | `mobile/spacedrop-admin` | Platform administrators |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Cloud 2023.0
- **Messaging**: Apache Kafka (KRaft mode — no Zookeeper)
- **Authentication**: Keycloak (OAuth2 / OpenID Connect)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway + OAuth2 Resource Server
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Observability**: OpenTelemetry, Micrometer, Grafana Tempo, Prometheus, Grafana
- **Database**: PostgreSQL (production), H2 (development)
- **Web UI**: React 19, Vite, React Router, keycloak-js
- **Mobile**: React Native with Expo
- **Infrastructure**: Docker Compose

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose

### Option 1: Run Everything with Docker Compose

Build the backend JARs first, then start the entire stack:

```bash
cd backend
mvn clean package -DskipTests

cd ..
docker compose up -d --build
```

This starts all services, databases, Keycloak, and the full observability stack.

| Component | URL |
|-----------|-----|
| **Web UI** | http://localhost:3000 |
| **API Gateway** | http://localhost:8080 |
| **Eureka Dashboard** | http://localhost:8761 |
| **Keycloak Admin** | http://localhost:8180 (admin / admin) |
| **Grafana** | http://localhost:3100 (admin / admin) |
| **Prometheus** | http://localhost:9090 |

### Option 2: Run Locally (Development)

#### Start Infrastructure

```bash
docker compose up -d kafka postgres-users postgres-parking postgres-booking postgres-payment postgres-keycloak keycloak otel-collector tempo prometheus grafana
```

This starts Kafka (KRaft mode), Keycloak, PostgreSQL databases, and the observability stack.

Keycloak admin console is available at http://localhost:8180 (admin / admin).

#### Build & Run Backend

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
cd ../payment-service && mvn spring-boot:run &
```

#### Run Web UI

```bash
cd web-ui
npm install
npm run dev
```

The web UI is available at http://localhost:3000.

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

### Payment Service (`/api/payments`)
- `POST /api/payments` — Initiate a payment
- `GET /api/payments/{id}` — Get payment by ID
- `GET /api/payments/booking/{bookingId}` — Get payment by booking
- `GET /api/payments/user/{userId}` — Get payments by user
- `PUT /api/payments/{id}/complete` — Complete a payment
- `PUT /api/payments/{id}/refund` — Refund a payment

## API Documentation (Swagger)

Each microservice exposes an interactive **Swagger UI** and an OpenAPI 3.0 spec. The API Gateway aggregates all service docs into a single Swagger UI.

| Service | Swagger UI | OpenAPI JSON |
|---------|-----------|-------------|
| **Gateway (aggregated)** | http://localhost:8080/swagger-ui.html | — |
| **User Service** | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| **Parking Service** | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |
| **Booking Service** | http://localhost:8083/swagger-ui.html | http://localhost:8083/v3/api-docs |
| **Payment Service** | http://localhost:8085/swagger-ui.html | http://localhost:8085/v3/api-docs |

The gateway Swagger UI lets you switch between services using the dropdown at the top of the page.

## Observability

The platform includes a full observability stack for distributed tracing, metrics, and dashboards — helping collect and analyze data about users, parking owners, bookings, and payments.

### Stack

| Component | Port | Purpose |
|-----------|------|---------|
| **OpenTelemetry Collector** | 4317 (gRPC), 4318 (HTTP) | Receives traces and metrics from all services |
| **Grafana Tempo** | 3200 | Distributed trace storage and querying |
| **Prometheus** | 9090 | Metrics scraping and storage |
| **Grafana** | 3100 | Dashboards and visualization |

### How It Works

- All services use **Micrometer Tracing** with the **OpenTelemetry bridge** to export traces and metrics via OTLP
- Each service exposes a `/actuator/prometheus` endpoint for Prometheus scraping
- Log correlation is enabled — every log line includes `[service-name, traceId, spanId]`
- The **OTel Collector** receives telemetry and forwards traces to Tempo and metrics to Prometheus
- **Grafana** provides pre-configured datasources for Prometheus and Tempo

### Accessing Dashboards

- **Grafana**: http://localhost:3100 (admin / admin) — Explore traces, create dashboards
- **Prometheus**: http://localhost:9090 — Query raw metrics
- **Tempo**: http://localhost:3200 — Trace search API

## Docker

All services are fully dockerized. Each backend service has a `Dockerfile` and the web UI uses a multi-stage build with nginx.

### Build & Run

```bash
# Build JARs first
cd backend && mvn clean package -DskipTests && cd ..

# Start everything
docker compose up -d --build
```

### Service Images

| Service | Base Image | Port |
|---------|-----------|------|
| discovery-service | eclipse-temurin:17-jre-alpine | 8761 |
| api-gateway | eclipse-temurin:17-jre-alpine | 8080 |
| user-service | eclipse-temurin:17-jre-alpine | 8081 |
| parking-service | eclipse-temurin:17-jre-alpine | 8082 |
| booking-service | eclipse-temurin:17-jre-alpine | 8083 |
| notification-service | eclipse-temurin:17-jre-alpine | 8084 |
| payment-service | eclipse-temurin:17-jre-alpine | 8085 |
| web-ui | node:18-alpine (build) + nginx:alpine | 3000 |