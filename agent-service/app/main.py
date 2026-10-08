from fastapi import FastAPI, Response, status

from app.config import Settings
from app.database import database_is_ready


def create_app(settings: Settings | None = None) -> FastAPI:
    settings = settings if settings is not None else Settings()
    api = FastAPI(
        title="Order and Subscription Support Agent",
        description="Phase 0: local API and PostgreSQL readiness checks.",
        version="0.1.0",
    )

    @api.get("/health/live", tags=["Health"])
    def liveness() -> dict[str, str]:
        return {
            "status": "UP",
            "service": settings.app_name,
            "environment": settings.app_env,
        }

    @api.get(
        "/health/ready",
        tags=["Health"],
        responses={503: {"description": "PostgreSQL is unavailable"}},
    )
    def readiness(response: Response) -> dict[str, str]:
        ready = database_is_ready(settings)
        if not ready:
            response.status_code = status.HTTP_503_SERVICE_UNAVAILABLE
        return {
            "status": "UP" if ready else "DOWN",
            "service": settings.app_name,
            "database": "UP" if ready else "DOWN",
        }

    return api


app = create_app()
