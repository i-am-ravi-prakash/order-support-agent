# Order and Subscription Support Agent

A personal portfolio project built in phases. The target is a customer-support agent that uses business APIs to investigate order and subscription issues, answers policy questions with citations, and performs approved actions.

## Current implementation: Phase 0

- FastAPI service with liveness and PostgreSQL readiness endpoints.
- Validated environment configuration.
- Local PostgreSQL through Docker Compose, with a persistent volume.
- Health contract and failure-handling tests.

The agent and order APIs are planned work. LLM integration starts in Phase 2; Java business APIs start in Phase 1. Use synthetic data throughout development.

## Start here

Read [Phase 0 guide](docs/PHASE_0_GUIDE.md) for every code file, local setup commands, verification, troubleshooting, and the first GitHub push.

Read [Roadmap](docs/ROADMAP.md) for features, completion gates, and commit/tag commands for every phase.

Read [Target architecture](docs/ARCHITECTURE.md) for component responsibilities and the planned agent workflow.

## Quick start

Run these commands from the repository root after installing Python 3.12 and starting Docker Desktop:

```bash
cp .env.example .env
python3.12 -m venv .venv
source .venv/bin/activate
python -m pip install -r agent-service/requirements-dev.txt
docker compose up -d --wait postgres
python -m uvicorn app.main:app --app-dir agent-service --host 127.0.0.1 --port 8000 --reload
```

In another terminal:

```bash
curl -i http://127.0.0.1:8000/health/live
curl -i http://127.0.0.1:8000/health/ready
```

Interactive API documentation: http://127.0.0.1:8000/docs

## Tests

From the repository root, with the virtual environment active:

```bash
PYTHONPATH=agent-service python -m unittest discover -s agent-service/tests -v
```

Tests use mocked database connections. Verify a real database separately with the commands in the Phase 0 guide.

## Stop the local environment

Stop Uvicorn with Ctrl+C. Stop PostgreSQL with:

```bash
docker compose down
```

The named volume retains database data.
