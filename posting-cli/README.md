# Posting CLI Endpoint Collections

This directory hierarchically organizes all endpoint request definitions for [posting](https://github.com/darrenburns/posting), mirrored directly according to the route hierarchy.

## Directory Hierarchy (Route Matching)

```text
posting-cli/
├── .env                              # Base environment (BASE_URL=http://localhost:7001)
├── actuator/                         # /actuator/*
│   └── health.posting.yaml           # GET /actuator/health
├── api/
│   └── public/                       # /api/public/*
│       └── ping.posting.yaml         # GET /api/public/ping
└── v3/                               # /v3/*
    └── api-docs.posting.yaml         # GET /v3/api-docs
```

## How to Run

Launch the interactive Posting TUI targeting the full collection:

```bash
posting --collection ./posting-cli
```

Or target a specific route hierarchy:

```bash
posting --collection ./posting-cli/api/public
```

## Rules for New Endpoints
Whenever a route is created in Spring Boot (e.g. `POST /api/v1/auth/login`), mirror the route hierarchy inside `posting-cli/`:
- Path: `posting-cli/api/v1/auth/login.posting.yaml`
