<<<<<<< HEAD
# Prowider Mini Lead Distribution System

Production-oriented full-stack lead allocation system with Spring Boot 3, PostgreSQL, Flyway, Next.js 15, Axios, SockJS, and STOMP.

## What is implemented

- Public lead form at `/request-service`
- Provider dashboard at `/dashboard` with live WebSocket refresh
- Test tools at `/test-tools`
- PostgreSQL schema managed by Flyway
- Duplicate lead protection with `UNIQUE(phone, service_type)`
- Assignment protection with `UNIQUE(lead_id, provider_id)`
- Transactional lead creation and allocation
- Pessimistic locks for allocation state and provider quota rows
- Persistent round-robin state in `allocation_state`
- Idempotent quota reset webhook using `webhook_events`

## Allocation Rules

Every lead gets exactly 3 providers.

- Service 1 always includes Provider 1, then round-robins Provider 2, 3, 4
- Service 2 always includes Provider 5, then round-robins Provider 6, 7, 8
- Service 3 always includes Provider 1 and Provider 4, then round-robins Provider 2, 3, 5, 6, 7, 8

The backend locks the service allocation row and provider rows during assignment. If quota is unavailable, the transaction rolls back, so a lead is not partially assigned.

## Local Run

Start PostgreSQL:

```bash
docker compose up -d
```

Run backend:

```bash
cd backend
mvn spring-boot:run
```

Run frontend:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:3000/dashboard`.

## API

- `POST /api/leads`
- `GET /api/leads`
- `GET /api/providers`
- `POST /api/webhook/reset-quota`
- `POST /api/test/generate-leads`
- WebSocket SockJS endpoint: `/ws`
- STOMP topic: `/topic/assignments`

Example webhook body:

```json
{
  "event_id": "quota-reset-2026-05-18"
}
```

## Deployment

Backend on Render:

- Build command: `cd backend && mvn clean package -DskipTests`
- Start command: `java -jar backend/target/prowider-backend-0.0.1-SNAPSHOT.jar`
- Set `DATABASE_URL` to a JDBC PostgreSQL URL, for example `jdbc:postgresql://host:5432/dbname`
- Set `DATABASE_USERNAME`
- Set `DATABASE_PASSWORD`
- Set `CORS_ALLOWED_ORIGINS` to your Vercel frontend URL

Frontend on Vercel:

- Root directory: `frontend`
- Build command: `npm run build`
- Set `NEXT_PUBLIC_API_BASE_URL` to your Render backend URL

Database on Neon or Supabase:

- Create a PostgreSQL database
- Use the pooled or direct connection details to set the backend JDBC URL and credentials
- Flyway creates tables and seed data automatically on backend startup

## Notes

The test endpoint intentionally creates leads concurrently to exercise locking. The quota reset webhook is idempotent: repeating the same `event_id` records only one effect.
=======
# lead-distribution-system
>>>>>>> e04d5471fa992098ed7cbf148036357e9c44ab2d
