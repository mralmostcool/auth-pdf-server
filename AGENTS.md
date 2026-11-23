# Project Guidelines - auth-pdf-server

## Workspace Boundary
- Strictly confined to this project directory (`/home/mrcool/projects/springboot/auth-pdf-server`).
- Never access, read, list, or edit files outside this repository root.

## Architecture & Technology Stack
- **Full Stack Project**:
  - **Backend**: Java Spring Boot 4.x (Java 21).
  - **Frontend**: Currently undefined (to be selected/added later).
  - **Infrastructure & Dependencies**: Controlled by Docker Compose (`docker-compose.yml`).
  - **API Testing**: `posting` TUI HTTP client using collections stored hierarchically in `./posting-cli/`.

## Port Allocation Rules
- Permitted ports: **7001, 7002, 7003 onwards**.
  - **7001**: Backend (Spring Boot)
  - **7002**: Frontend (once defined)
  - **7003+**: Supporting services (Database: 7003, Redis: 7004, etc. in Docker Compose)
- Do NOT use standard default ports (such as 8080, 3000, 5432) for host bindings; map them to `7001`, `7002`, `7003+`.

## API Testing with Posting CLI
- All endpoint requests are organized into declarative YAML files inside `./posting-cli/`.
- Maintain requests structured hierarchically to mirror route paths:
  - `posting-cli/api/public/ping.posting.yaml` for `/api/public/ping`
  - `posting-cli/actuator/health.posting.yaml` for `/actuator/health`
  - `posting-cli/v3/api-docs.posting.yaml` for `/v3/api-docs`
- Run interactive testing using: `posting --collection ./posting-cli`.
