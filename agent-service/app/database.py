import logging

import psycopg

from app.config import Settings


logger = logging.getLogger(__name__)


def database_is_ready(settings: Settings) -> bool:
    """Check connectivity without returning connection details to the caller."""
    try:
        with psycopg.connect(
            host=settings.postgres_host,
            port=settings.postgres_port,
            dbname=settings.postgres_db,
            user=settings.postgres_user,
            password=settings.postgres_password.get_secret_value(),
            connect_timeout=3,
            options="-c statement_timeout=3000",
            autocommit=True,
        ) as connection:
            return connection.execute("SELECT 1").fetchone() == (1,)
    except psycopg.Error as exc:
        logger.warning("Database readiness check failed: %s", type(exc).__name__)
        return False
