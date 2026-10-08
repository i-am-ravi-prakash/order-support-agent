# Phase 1: business service

Java 21, Spring Boot 3.5.16, Spring JDBC, Flyway, PostgreSQL 16.

Run PostgreSQL from the repository root:

```bash
docker compose up -d --wait postgres
```

Run the business service from this directory (`business-service`).
The existing root `.env` supplies database settings.

```bash
mvn test
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The API runs at http://127.0.0.1:8081.

Docker Desktop must be running for `mvn test`.
Testcontainers starts a separate disposable PostgreSQL database.

The business schema contains customers, single-product demo orders,
one shipment per order, and subscriptions.

Flyway owns schema changes. Demo rows are applied with the local profile.
Create new migrations for subsequent changes; do not edit applied migrations.

Demo dates are a fixed synthetic snapshot.

## APIs

| GET endpoint | Result |
| --- | --- |
| /api/v1/customers/1 | Synthetic customer |
| /api/v1/customers/1/orders | Paginated orders |
| /api/v1/customers/1/orders/101 | Printer order |
| /api/v1/customers/1/orders/101/shipment | Delayed shipment |
| /api/v1/customers/1/shipments/TRK-101 | Tracking lookup |
| /api/v1/customers/1/subscriptions | Paginated subscriptions |
| /api/v1/customers/1/subscriptions/501 | Monthly printer plan |
| /actuator/health/liveness | Process health |
| /actuator/health/readiness | Process and database health |

Lists accept page and size parameters. Pages start at zero.
Unknown resources return 404. Invalid parameters return 400.
Database failures return a generic 503.

Customer IDs currently select demo accounts.
JWT authentication arrives in Phase 3.
Phase 2 adds the AI agent and tools calling these APIs.