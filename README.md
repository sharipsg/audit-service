# Audit Service

A simple Spring Boot application demonstrating entity auditing using the **audit-spring-boot-starter** library.

## Overview

This application showcases how to:
- **Audit entity changes** - Automatically track CREATE and UPDATE operations on entities
- **JWT Authentication** - Users obtain a token by providing their email
- **View Audit History** - Query audit logs for any tracked entity

## Tech Stack

- **Backend**: Spring Boot 4.x, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Audit**: audit-spring-boot-starter (custom library)
- **Frontend**: Vue.js 3 (UI generated using AI)

## How It Works

### 1. Authentication
Users log in by requesting a JWT token with their email. The token is stored in browser localStorage and used for subsequent API requests.

### 2. Entity Auditing
The `Employee` entity is annotated with `@Audited`. Any create or update operation is automatically tracked by the audit library, recording:
- Who made the change (`changed_by`)
- When it happened (`changed_at`)
- What changed (field-level diff for updates)

### 3. Audit Queries
Audit history can be retrieved via the `/api/audit/entity/{entity}/{id}` endpoint.

## Running the Application

### Backend
```bash
./mvnw spring-boot:run
```
Runs on `http://localhost:8090`

### Frontend
```bash
cd auditservice-ui
npm install
npm run dev
```
Runs on `http://localhost:3000`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/token` | Generate JWT token |
| GET | `/api/employees` | List all employees |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| GET | `/api/audit/entity/{entity}/{id}` | Get audit history |

## Configuration

Database connection is configured in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:9000/general
spring.datasource.username=xxx
spring.datasource.password=xxx
```

