# Posting Collections for auth-pdf-server

This directory contains endpoint definitions for [posting](https://github.com/darrenburns/posting), the terminal-based HTTP client TUI.

## Structure

```text
posting/
├── .env                       # Base URL and local testing environment variables
├── actuator/
│   └── health.posting.yaml    # Actuator health endpoint
├── public/
│   └── ping.posting.yaml      # Public test endpoint
└── docs/
    └── openapi-docs.posting.yaml # OpenAPI JSON specification endpoint
```

## How to Run

Launch the interactive Posting TUI loaded with this collection:

```bash
posting --collection ./posting
```

Or target a specific subfolder:

```bash
posting --collection ./posting/public
```

## Adding New Endpoints
Whenever a new controller or endpoint is implemented in Spring Boot, add a corresponding `.posting.yaml` file in this directory under its domain module folder.
