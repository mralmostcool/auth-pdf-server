# Workspace Rules: auth-pdf-server

## 1. Scope & Isolation Boundary (STRICT)
- **Project Boundary**: The agent MUST NEVER inspect, read, modify, or create files outside this project directory (`/home/mrcool/projects/springboot/auth-pdf-server` or relative `./`).
- Do NOT search or traverse parent directories or global system files outside this project root.
- Keep all project-related assets, configs, logs, and artifacts contained strictly within this project repository.

## 2. Port Allocation Rules
Only the following port ranges are allowed for services in this project:
- **Port 7001**: Reserved / Primary Backend Service (Spring Boot application).
- **Port 7002**: Reserved (Frontend dev server or secondary service).
- **Port 7003+**: Allowed for auxiliary services and containers (PostgreSQL: 7003, Redis: 7004, etc.).
- **Rule**: Never bind or expose ports outside `7001, 7002, 7003+`. Map default container ports to these allocated ports.

## 3. Architecture & Tech Stack Guidelines
- **Architecture**: Full-stack application.
- **Backend**: Java Spring Boot 4.x (Java 21 LTS). Follow modular, clean architecture with Spring Modulith, JPA, Flyway, and Spring Security.
- **Frontend**: Currently undefined.
- **Orchestration**: Everything is orchestrated via `docker-compose.yml` with central environment variables stored in `.env` and `.env.example`.

## 4. API Testing with `posting` (`posting-cli/`)
- We use **`posting`** (the terminal TUI HTTP client) for interactive endpoint testing and manual verification.
- **Location**: Store posting requests and collections inside the `posting-cli/` folder in the project root.
- **Route-Based Hierarchy**: Request files must mirror the actual URL paths:
  - Example: route `/api/public/ping` -> file `posting-cli/api/public/ping.posting.yaml`
  - Example: route `/actuator/health` -> file `posting-cli/actuator/health.posting.yaml`
  - Example: route `/v3/api-docs` -> file `posting-cli/v3/api-docs.posting.yaml`
- **Environment**: Maintain a posting environment file `posting-cli/.env` referencing `BASE_URL=http://localhost:7001` so that requests can be executed directly with `posting --collection ./posting-cli`.
- Whenever new endpoints are created or modified in the backend, corresponding `.posting.yaml` request files must be created or updated within matching folder hierarchy.
