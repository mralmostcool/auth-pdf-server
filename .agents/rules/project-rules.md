# Workspace Rules: auth-pdf-server

## 1. Scope & Isolation Boundary (STRICT)
- **Project Boundary**: The agent MUST NEVER inspect, read, modify, or create files outside this project directory (`/home/mrcool/projects/springboot/auth-pdf-server` or relative `./`).
- Do NOT search or traverse parent directories or global system files outside this project root.
- Keep all project-related assets, configs, logs, and artifacts contained strictly within this project repository.

## 2. Port Allocation Rules
Only the following port ranges are allowed for services in this project:
- **Port 7001**: Reserved / Primary Backend Service (e.g., Spring Boot application).
- **Port 7002**: Reserved (e.g., Frontend dev server or secondary service).
- **Port 7003+**: Allowed for additional services, databases, or support containers (e.g., PostgreSQL, Redis, Mailhog, mock servers, etc.).
- **Rule**: Never bind or expose ports outside `7001, 7002, 7003+` (e.g., standard ports like 8080, 3000, 5432 should be mapped to the `7001+` range in `docker-compose.yml` or local configurations).

## 3. Architecture & Tech Stack Guidelines
- **Architecture**: Full-stack application.
- **Backend**: Java Spring Boot. Follow standard Spring Boot project layout, idiomatic conventions, and clean architecture (controllers, services, repositories, DTOs).
- **Frontend**: Currently undefined. When initialized, it should integrate cleanly with the backend APIs and follow standard modern frontend tooling.
- **Orchestration**: Everything else (databases, caches, third-party mock services, dependencies) must be controlled and orchestrated via `docker-compose.yml`.
- All environment variables and port mappings for containerized services must respect the assigned port rules.
