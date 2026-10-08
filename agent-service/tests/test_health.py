import os
import unittest
from unittest.mock import patch

import psycopg
from fastapi.testclient import TestClient


# Tests run without a .env file and never contact a real database.
with patch.dict(os.environ, {"POSTGRES_PASSWORD": "unit-test-only"}):
    from app.config import Settings
    from app.main import create_app


class HealthTests(unittest.TestCase):
    def setUp(self):
        settings = Settings(
            _env_file=None,
            app_env="test",
            postgres_password="unit-test-only",
        )
        self.client = TestClient(create_app(settings))
        self.addCleanup(self.client.close)

    def test_liveness_does_not_depend_on_database(self):
        with patch("app.database.psycopg.connect") as connect:
            response = self.client.get("/health/live")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()["status"], "UP")
        connect.assert_not_called()

    def test_readiness_succeeds_when_database_responds(self):
        with patch("app.database.psycopg.connect") as connect:
            connection = connect.return_value.__enter__.return_value
            connection.execute.return_value.fetchone.return_value = (1,)
            response = self.client.get("/health/ready")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()["database"], "UP")

    def test_database_failure_returns_503_without_sensitive_details(self):
        with patch(
            "app.database.psycopg.connect",
            side_effect=psycopg.OperationalError("password=do-not-expose"),
        ):
            with self.assertLogs("app.database", level="WARNING") as logs:
                response = self.client.get("/health/ready")
        self.assertEqual(response.status_code, 503)
        self.assertEqual(response.json()["status"], "DOWN")
        self.assertNotIn("do-not-expose", response.text)
        self.assertNotIn("do-not-expose", " ".join(logs.output))


if __name__ == "__main__":
    unittest.main()
