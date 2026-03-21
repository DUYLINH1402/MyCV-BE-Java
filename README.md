<div align="center">

# Portfolio API Backend

### Personal Portfolio & CV - RESTful API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![AWS](https://img.shields.io/badge/AWS-EC2%20%2B%20RDS-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white)](https://aws.amazon.com/)

<p align="center">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square" alt="License">
  <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square" alt="PRs Welcome">
  <img src="https://img.shields.io/badge/API-Documented-blue.svg?style=flat-square" alt="API Docs">
</p>

---

**Backend RESTful API** cho website Portfolio/CV cá nhân. Cho phép nhà tuyển dụng xem thông tin, dự án, kỹ năng và gửi tin nhắn liên hệ. Admin quản lý nội dung qua dashboard với JWT authentication.

[Features](#1-features) •
[Technologies](#2-technologies) •
[Architecture](#3-system-architecture) •
[Installation](#5-installation) •
[API Docs](#6-api-documentation) •
[Database](#7-database-schema)

</div>

---

## 1. Features

<table>
<tr>
<td>

### 🔐 Authentication & Security
- JWT Stateless Authentication (HS256)
- Role-based URL prefix (`/public` vs `/admin`)
- BCrypt password hashing
- Change password with validation
- CORS configuration (env-specific)

</td>
<td>

### 👤 Profile Management
- Single-owner profile (1 record)
- Full personal info: bio, summary, education
- Social links: GitHub
- Admin update via dashboard

</td>
</tr>
<tr>
<td>

### 💼 Project Management
- CRUD with soft delete
- Featured projects & categories
- JSONB fields (description, technologies, gallery)
- Status & display order control

</td>
<td>

### 🛠️ Skill Management
- CRUD with soft delete
- Category: Frontend, Backend, Database, Tools, DevOps, Other
- Skill levels: Low, Medium, High
- Priority-based ordering

</td>
</tr>
<tr>
<td>

### 📬 Contact Form
- Receive messages from recruiters
- Spam protection (5-min cooldown per email)
- XSS prevention in email content
- Client IP tracking
- Async email notification via Brevo

</td>
<td>

### ⚡ Performance & Caching
- Caffeine local cache (TTL: 10 min)
- Auto cache eviction on write/delete
- Admin manual cache clear API
- Async email sending (non-blocking)
- HikariCP connection pooling

</td>
</tr>
</table>

---

## 2. Technologies

<div align="center">

| Category | Technologies |
|----------|-------------|
| **Framework** | ![Spring Boot](https://img.shields.io/badge/Spring_Boot_4.0.3-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white) |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-4169E1?style=flat-square&logo=postgresql&logoColor=white) ![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=flat-square&logo=flyway&logoColor=white) |
| **Authentication** | ![JWT](https://img.shields.io/badge/JWT_(jjwt_0.12.6)-000000?style=flat-square&logo=json-web-tokens&logoColor=white) |
| **Caching** | ![Caffeine](https://img.shields.io/badge/Caffeine_Cache-6DB33F?style=flat-square&logo=spring&logoColor=white) |
| **Documentation** | ![Swagger](https://img.shields.io/badge/Swagger_UI-85EA2D?style=flat-square&logo=swagger&logoColor=black) ![OpenAPI](https://img.shields.io/badge/OpenAPI_3.0-6BA539?style=flat-square&logo=openapi-initiative&logoColor=white) |
| **Email** | ![Brevo](https://img.shields.io/badge/Brevo_(Sendinblue)-0B3D91?style=flat-square&logo=sendinblue&logoColor=white) |
| **DevOps** | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white) ![AWS](https://img.shields.io/badge/AWS_EC2/RDS-FF9900?style=flat-square&logo=amazon-aws&logoColor=white) |
| **Tools** | ![MapStruct](https://img.shields.io/badge/MapStruct_1.6.3-E34F26?style=flat-square&logo=java&logoColor=white) ![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=flat-square&logo=java&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white) |

</div>

---

## 3. System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND CLIENT                          │
│              (Firebase Hosting - React/Vue)                 │
│         https://portfolio-f2abd.web.app                     │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  CORS FILTER (Highest Priority)             │
│              Environment-specific configuration             │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│               JWT AUTHENTICATION FILTER                     │
│         Token validation → Set SecurityContext               │
│                                                             │
│    /v1/public/**  → permitAll (No auth required)            │
│    /v1/admin/**   → hasRole("ADMIN") (JWT required)         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   CONTROLLER LAYER                          │
│         Lean controllers (validate + delegate)              │
│    Return ResponseEntity<ApiResponse<T>>                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                            │
│        Business Logic | @Transactional | Caching            │
│              MapStruct (Entity ↔ DTO)                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  REPOSITORY LAYER                           │
│              Spring Data JPA + Hibernate                    │
└──────────────┬──────────────────────────┬───────────────────┘
               │                          │
               ▼                          ▼
┌──────────────────────┐    ┌─────────────────────────────────┐
│   PostgreSQL 16      │    │       Caffeine Cache            │
│   (AWS RDS / Local)  │    │  (Profile, Projects, Skills)    │
│                      │    │   TTL: 10min | Max: 500         │
└──────────────────────┘    └─────────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────────────────────┐
│                  EXTERNAL SERVICES                          │
├─────────────────────────────────────────────────────────────┤
│              Brevo (Sendinblue) REST API v3                 │
│          Async email notification for contact form          │
└─────────────────────────────────────────────────────────────┘
```

### Deployment Architecture

```
[Frontend: Firebase Hosting] ←→ [Backend: AWS EC2 (Docker)] ←→ [DB: AWS RDS PostgreSQL]
                                        ↓
                               [Brevo Email API] → Email thông báo cho Admin
```

---

## 📁 4. Project Structure

```
src/main/java/com/linhnguyen/portfolio_api/
├── 📄 PortfolioApiApplication.java          # Main entry point
├── 📂 common/
│   └── ApiResponse.java                     # Unified API response wrapper
├── 📂 config/
│   ├── AsyncConfig.java                     # @Async for email service
│   ├── BrevoConfig.java                     # Brevo email configuration
│   ├── CacheConfig.java                     # Caffeine cache setup
│   ├── CorsConfig.java                      # CORS filter (highest priority)
│   ├── CorsProperties.java                  # CORS properties from yml
│   ├── JpaAuditingConfig.java               # JPA Auditing (createdAt, updatedAt...)
│   ├── OpenApiConfig.java                   # Swagger/OpenAPI + JWT scheme
│   ├── RestTemplateConfig.java              # RestTemplate for Brevo API
│   └── SecurityConfig.java                  # Spring Security configuration
├── 📂 controller/
│   ├── AuthController.java                  # POST /v1/public/auth/login
│   ├── ContactController.java               # POST /v1/public/contact
│   ├── HealthController.java                # GET  /v1/public/health
│   ├── ProfileController.java               # GET  /v1/public/profile
│   ├── ProjectController.java               # GET  /v1/public/projects/**
│   ├── SkillController.java                 # GET  /v1/public/skills/**
│   └── 📂 admin/
│       ├── AdminAuthController.java         # PUT  /v1/admin/auth/change-password
│       ├── AdminProfileController.java      # GET/PUT /v1/admin/profile
│       ├── AdminProjectController.java      # CRUD /v1/admin/projects/**
│       └── AdminSkillController.java        # CRUD /v1/admin/skills/**
├── 📂 dto/
│   ├── 📂 request/                          # Input DTOs (validation + @Schema)
│   └── 📂 response/                         # Output DTOs
├── 📂 entity/
│   ├── BaseEntity.java                      # Abstract: audit fields + soft delete
│   ├── AdminCredential.java                 # Admin login credentials
│   ├── ContactMessage.java                  # Contact form messages
│   ├── Profile.java                         # Portfolio owner profile
│   ├── Project.java                         # Projects (JSONB support)
│   ├── Skill.java                           # Skills with category & level
│   ├── SkillCategory.java                   # Enum: FRONTEND, BACKEND, DATABASE...
│   └── SkillLevel.java                      # Enum: LOW, MEDIUM, HIGH
├── 📂 exception/
│   ├── BusinessException.java               # Base exception
│   ├── DuplicateResourceException.java      # HTTP 409
│   ├── ResourceNotFoundException.java       # HTTP 404
│   ├── ErrorResponse.java                   # Standardized error format
│   └── 📂 handler/
│       └── GlobalExceptionHandler.java      # @RestControllerAdvice
├── 📂 mapper/                               # MapStruct mappers (Entity ↔ DTO)
├── 📂 repository/                           # Spring Data JPA repositories
├── 📂 security/
│   ├── CustomUserDetailsService.java        # Load admin from DB
│   ├── JwtAuthenticationEntryPoint.java     # 401 JSON response
│   ├── JwtAuthenticationFilter.java         # JWT filter per request
│   └── JwtTokenProvider.java               # JWT create/validate (HS256)
├── 📂 service/
│   ├── AuthService.java                     # Login + change password
│   ├── BrevoEmailService.java               # Async email via Brevo API
│   ├── ContactService.java                  # Contact + spam protection
│   ├── ProfileService.java                  # Profile CRUD + cache
│   ├── ProjectService.java                  # Project CRUD + cache
│   └── SkillService.java                    # Skill CRUD + cache
└── 📂 util/
    └── PasswordHashGenerator.java           # BCrypt hash utility
```

---

## 5. Installation

### System Requirements

- **Java** 17+
- **Maven** 3.8+
- **Docker** & **Docker Compose** (recommended)
- **PostgreSQL** 16

### Installation with Docker (Recommended)

1. **Clone repository**
   ```bash
   git clone https://github.com/DUYLINH1402/MyCV-BE-Java.git
   cd portfolio-api
   ```

2. **Create `.env` file**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables** in `.env` file:
   ```env
   # Database
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=portfolio_db
   DB_USERNAME=postgres
   DB_PASSWORD=your_password

   # JWT Security
   JWT_SECRET=your-jwt-secret-key-must-be-at-least-256-bits
   JWT_EXPIRATION=86400000

   # Admin
   ADMIN_SECRET_TOKEN=your-admin-secret-token

   # Brevo Email
   BREVO_API_KEY=your_brevo_api_key
   BREVO_ENABLED=true
   BREVO_RECIPIENT_EMAIL=your_email@example.com
   BREVO_RECIPIENT_NAME=Your Name
   BREVO_SENDER_EMAIL=noreply@yourportfolio.com
   BREVO_SENDER_NAME=Portfolio Contact

   # CORS
   CORS_ALLOWED_ORIGINS=https://portfolio-f2abd.web.app
   ```

4. **Start PostgreSQL with Docker Compose**
   ```bash
   docker compose up -d
   ```

5. **Build and run the application**
   ```bash
   ./mvnw clean install -DskipTests
   ./mvnw spring-boot:run
   ```

6. **Verify the application**
   ```
   http://localhost:8080/api/v1/public/health
   ```

7. **Access Swagger UI**
   ```
   http://localhost:8080/api/swagger-ui.html
   ```

### Production Deployment (AWS EC2 + RDS)

1. **Setup EC2 instance**
   ```bash
   chmod +x scripts/ec2-setup.sh
   ./scripts/ec2-setup.sh
   ```

2. **Configure `.env` on EC2** with production values (RDS endpoint, secrets, etc.)

3. **Deploy with Docker Compose**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

4. **Check logs**
   ```bash
   docker logs -f portfolio-api
   ```

---

## 6. API Documentation

After starting, access **Swagger UI** at:

```
http://localhost:8080/api/swagger-ui.html
```


## 7. Database Schema

### Entity Relationship

```
BaseEntity (abstract: createdAt, updatedAt, createdBy, updatedBy, isDeleted)
├── Profile            → 1 record (portfolio owner)
├── Project            → Multiple records (JSONB: fullDescription, technologies, gallery)
├── Skill              → Multiple records (enum: SkillCategory + SkillLevel)
├── ContactMessage     → Multiple records (messages from recruiters)
└── AdminCredential    → 1 record (separated from Profile - SoC)
```

### Tables Overview

| Table | Key Columns | Notes |
|-------|-------------|-------|
| **profile** | full_name, title, bio, professional_summary, experience_years, email, github_url, linkedin_url, avatar_url | Single record |
| **projects** | title, short_description, full_description(JSONB), technologies(JSONB), gallery(JSONB), category, status, is_featured | Supports rich content |
| **skills** | name, category(enum), level(enum), priority | Categorized & leveled |
| **contact_messages** | sender_name, sender_email, subject, message, is_read, email_sent, sender_ip | Spam protected |
| **admin_credentials** | email, password(BCrypt), role, is_active | Single admin |

### Migration

Database migrations are managed by **Flyway**:

```
src/main/resources/db/migration/
└── V1__Create_contact_messages_table.sql
```

> Other tables are currently managed by Hibernate `ddl-auto: update`.

---

## 8. Security

| Feature | Implementation |
|---------|---------------|
| **Authentication** | JWT Stateless (HS256, default expiry: 24h) |
| **Authorization** | Role-based URL prefix (`/public` vs `/admin`) |
| **Password** | BCrypt hashing |
| **CORS** | Environment-specific, credentials support |
| **Spam Protection** | 5-minute cooldown per email on contact form |
| **XSS Prevention** | HTML escape in email content |
| **IP Tracking** | Client IP logged for contact messages |
| **SQL Injection** | Protected via JPA/Hibernate parameterized queries |

---

## 9. Caching Strategy

The project uses **Caffeine** for local caching:

| Cache Name | TTL | Max Size | Description |
|------------|-----|----------|-------------|
| `profile` | 10 min | 500 | Portfolio owner profile |
| `projects` | 10 min | 500 | Project listings & details |
| `skills` | 10 min | 500 | Skill listings & details |

**Cache behavior:**
- **Read:** `@Cacheable` — Cache on first read, serve from cache on subsequent reads
- **Write/Delete:** `@CacheEvict(allEntries = true)` — Evict entire cache on data mutation
- **Manual clear:** `DELETE /api/v1/admin/profile/cache` — Admin can force cache clear

---

## 10. Environment Variables

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `DB_HOST` | Database host |O | — |
| `DB_PORT` | Database port | X | `5432` |
| `DB_NAME` | Database name |O | — |
| `DB_USERNAME` | Database user |O | — |
| `DB_PASSWORD` | Database password |O | — |
| `JWT_SECRET` | JWT signing key (≥256 bits) |O | — |
| `JWT_EXPIRATION` | JWT token TTL (ms) | X | `86400000` (24h) |
| `ADMIN_SECRET_TOKEN` | Admin secret token |O | — |
| `BREVO_API_KEY` | Brevo API key |O | — |
| `BREVO_ENABLED` | Enable/disable email | X | `true` |
| `BREVO_RECIPIENT_EMAIL` | Notification recipient email |O | — |
| `BREVO_RECIPIENT_NAME` | Notification recipient name |O | — |
| `BREVO_SENDER_EMAIL` | Sender email (verified on Brevo) |O | — |
| `BREVO_SENDER_NAME` | Sender display name |O | — |
| `SPRING_PROFILES_ACTIVE` | Active profile | X | `dev` |
| `SERVER_PORT` | Server port | X | `8080` |
| `CORS_ALLOWED_ORIGINS` | Allowed origins (comma-separated) | X | `https://portfolio-f2abd.web.app` |

---

## 11. Docker

### Multi-Stage Build

The project uses a **multi-stage Dockerfile** for optimized images:

```
Stage 1: Maven build (maven:3.9-eclipse-temurin-17-alpine)
    → Compile & package .jar

Stage 2: Runtime (eclipse-temurin:25-jre-alpine)
    → Copy .jar & run (lightweight JRE-only image)
```

### Docker Compose Files

| File | Purpose |
|------|---------|
| `compose.yaml` | Local development — PostgreSQL only |
| `docker-compose.prod.yml` | Production — App container (EC2 + RDS) |

---


## 12. Contact

<div align="center">

[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:duylinh63b5@gmail.com)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/DUYLINH1402)

</div>

---

<div align="center">

###  Star this repo if you find it helpful!

Made with love️ by [Nguyen Duy Linh](https://github.com/DUYLINH1402)

</div>

