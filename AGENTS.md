# Project Guidelines - auth-pdf-server

## Workspace Boundary
- Strictly confined to this project directory (`/home/mrcool/projects/springboot/auth-pdf-server`).
- Never access, read, list, or edit files outside this repository root.

## Architecture & Technology Stack
- **Full Stack Project**:
  - **Backend**: Java Spring Boot.
  - **Frontend**: Currently undefined (to be selected/added later).
  - **Infrastructure & Dependencies**: Controlled by Docker Compose (`docker-compose.yml`).

## Port Allocation Rules
- Permitted ports: **7001, 7002, 7003 onwards**.
  - **7001**: Backend (Spring Boot)
  - **7002**: Frontend (once defined)
  - **7003+**: Supporting services (Database, Redis, etc. in Docker Compose)
- Do NOT use standard default ports (such as 8080, 3000, 5432) for host bindings; map them to `7001`, `7002`, `7003+`.
