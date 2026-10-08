# Build roadmap and GitHub checkpoints

The core goal is a working Order and Subscription Support Agent that investigates customer issues, uses trusted business APIs, retrieves policy evidence, and executes approved actions. Build each phase locally, complete its gate, and then commit it. AWS deployment starts after the local acceptance checks pass.

Each phase will be taught with: the goal, new or changed files with complete contents, commands to run, expected outputs, failure cases, a completion checklist, and a GitHub checkpoint.

## Feature roadmap

| Phase | Outcome | Completion gate |
| --- | --- | --- |
| 0 | **Local foundation**: Python API, validated configuration, PostgreSQL Compose, liveness/readiness checks. | All three tests pass; real PostgreSQL is healthy; /health/live and /health/ready return HTTP 200. |
| 1 | **Java business APIs**: Java 21 and Spring Boot; PostgreSQL migrations; synthetic customers, orders, shipments and subscriptions; read APIs with explicit errors. | Known and unknown tracking IDs have defined responses; shipment/subscription APIs work; business API tests pass. |
| 2 | **Working AI agent**: Agno + OpenAI; narrow tools that call Java APIs; order/shipment/subscription lookup; persistent user/session identifiers; typed tool responses. | The agent asks for missing IDs, queries actual demo records, handles unknown IDs, and resumes a saved conversation. |
| 3 | **Authentication and isolation**: JWT identity, resource ownership, separate customer sessions, role checks, scoped tool access, sensitive-data masking. | Customer A cannot access customer B's orders, sessions, approvals or tickets through prompts or direct API calls. |
| 4 | **Policy retrieval**: RAG over versioned delivery/cancellation policies; PostgreSQL pgvector; metadata filtering; visible citations; explicit insufficient-evidence responses. | Answers cite the correct document/version; unsupported questions do not fabricate policy. |
| 5 | **Approved business actions**: Support tickets, cancellation/refund eligibility previews, persistent pending approvals, confirmation/rejection, idempotency and audit history. | Approval survives restart; rejected actions do not execute; retries create one effect; authorization and eligibility are rechecked at execution. |
| 6 | **Proactive shipment support**: Kafka shipment events, transactional outbox, idempotent consumers, bounded retries and dead-letter handling; background investigation and support drafts. | A synthetic delayed-shipment event produces one support draft; duplicate delivery does not duplicate actions; failed events are inspectable. |
| 7 | **Reliability and demo**: Regression/evaluation cases, tool traces, timeout handling, latency/token metrics, basic rate limits, Dockerfiles, local chat/approval screen and GitHub CI. | Local acceptance scenarios and CI pass; dependency outages have controlled responses; publish measured results with the test dataset. |
| 8 | **AWS learning deployment**: Deploy the locally verified containers on EC2; ECR images, IAM, security groups, CloudWatch and managed secrets; repeatable setup and teardown. | Authenticated end-to-end demo works on AWS; restart and error paths are checked; setup and cleanup commands are documented. |
| 9 | **Optional AWS deployment upgrade**: ECS Fargate, ALB, RDS PostgreSQL, private networking, S3 policy documents, Terraform and GitHub Actions using OIDC. | Cloud deployment is repeatable; health checks and logs work; rollback and cost/cleanup instructions are tested. |

## Scope choices

- Python owns agent orchestration; Java owns business rules and transactions.
- Begin with one business service. Separate order and subscription services only if a later requirement justifies it.
- Shipping and payment integrations use local simulators initially. Refund actions write simulated records rather than transfer real money.
- Notifications are shown in the demo UI or an internal notification record until an actual external integration is deliberately enabled.
- Kafka enters in Phase 6. Each phase should add a working use case before adding another dependency.
- Tests assert business behaviour and access boundaries. Agent evaluations check response evidence and tool selection/arguments.
- A multi-agent extension is optional. Evaluate whether a specialist agent improves quality or context isolation before splitting the workflow.

## Phase 0: first GitHub push

Create a new, empty GitHub repository named `order-support-agent`. Leave GitHub's README, license and .gitignore initialization unchecked because these files are already local. Copy its SSH URL after configuring GitHub SSH authentication. The HTTPS URL also works with GitHub's supported authentication methods.

From the local project root, after the Phase 0 completion gate passes:

```bash
git init -b main
git add .
git status --short
git check-ignore .env .venv/
git diff --cached --stat
git commit -m "Phase 0: local API and database foundation"
git remote add origin git@github.com:YOUR_GITHUB_USERNAME/order-support-agent.git
git push -u origin main
git tag -a phase-0 -m "Phase 0 complete"
git push origin phase-0
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub username. `.env` and `.venv/` must be ignored; `.env.example` should be committed. If Git asks for identity, configure your name and GitHub-associated email for this repository before committing. Do not rerun `git init` or `git remote add` for later phases.

For HTTPS, replace the remote-add line with:

```bash
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/order-support-agent.git
```

Run one remote-add command, choosing the URL that matches your configured GitHub authentication.

## Phase 1: Java business APIs

Implement: Java 21 and Spring Boot; PostgreSQL migrations; synthetic customers, orders, shipments and subscriptions; read APIs with explicit errors.

Complete before committing: Known and unknown tracking IDs have defined responses; shipment/subscription APIs work; business API tests pass.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 1: order shipment and subscription apis"
git push origin main
git tag -a phase-1 -m "Phase 1 complete"
git push origin phase-1
```

## Phase 2: Working AI agent

Implement: Agno + OpenAI; narrow tools that call Java APIs; order/shipment/subscription lookup; persistent user/session identifiers; typed tool responses.

Complete before committing: The agent asks for missing IDs, queries actual demo records, handles unknown IDs, and resumes a saved conversation.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 2: ai agent with api tools and session history"
git push origin main
git tag -a phase-2 -m "Phase 2 complete"
git push origin phase-2
```

## Phase 3: Authentication and isolation

Implement: JWT identity, resource ownership, separate customer sessions, role checks, scoped tool access, sensitive-data masking.

Complete before committing: Customer A cannot access customer B's orders, sessions, approvals or tickets through prompts or direct API calls.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 3: authentication ownership and session isolation"
git push origin main
git tag -a phase-3 -m "Phase 3 complete"
git push origin phase-3
```

## Phase 4: Policy retrieval

Implement: RAG over versioned delivery/cancellation policies; PostgreSQL pgvector; metadata filtering; visible citations; explicit insufficient-evidence responses.

Complete before committing: Answers cite the correct document/version; unsupported questions do not fabricate policy.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 4: policy retrieval with citations"
git push origin main
git tag -a phase-4 -m "Phase 4 complete"
git push origin phase-4
```

## Phase 5: Approved business actions

Implement: Support tickets, cancellation/refund eligibility previews, persistent pending approvals, confirmation/rejection, idempotency and audit history.

Complete before committing: Approval survives restart; rejected actions do not execute; retries create one effect; authorization and eligibility are rechecked at execution.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 5: approval workflows and idempotent actions"
git push origin main
git tag -a phase-5 -m "Phase 5 complete"
git push origin phase-5
```

## Phase 6: Proactive shipment support

Implement: Kafka shipment events, transactional outbox, idempotent consumers, bounded retries and dead-letter handling; background investigation and support drafts.

Complete before committing: A synthetic delayed-shipment event produces one support draft; duplicate delivery does not duplicate actions; failed events are inspectable.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 6: event-driven shipment support"
git push origin main
git tag -a phase-6 -m "Phase 6 complete"
git push origin phase-6
```

## Phase 7: Reliability and demo

Implement: Regression/evaluation cases, tool traces, timeout handling, latency/token metrics, basic rate limits, Dockerfiles, local chat/approval screen and GitHub CI.

Complete before committing: Local acceptance scenarios and CI pass; dependency outages have controlled responses; publish measured results with the test dataset.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 7: reliability observability and local demo"
git push origin main
git tag -a phase-7 -m "Phase 7 complete"
git push origin phase-7
```

## Phase 8: AWS learning deployment

Implement: Deploy the locally verified containers on EC2; ECR images, IAM, security groups, CloudWatch and managed secrets; repeatable setup and teardown.

Complete before committing: Authenticated end-to-end demo works on AWS; restart and error paths are checked; setup and cleanup commands are documented.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 8: aws learning deployment"
git push origin main
git tag -a phase-8 -m "Phase 8 complete"
git push origin phase-8
```

## Phase 9: Optional AWS deployment upgrade

Implement: ECS Fargate, ALB, RDS PostgreSQL, private networking, S3 policy documents, Terraform and GitHub Actions using OIDC.

Complete before committing: Cloud deployment is repeatable; health checks and logs work; rollback and cost/cleanup instructions are tested.

Run the phase-specific tests and local acceptance commands supplied with that phase. Then, from the project root:

```bash
git add .
git status --short
git diff --cached --stat
git commit -m "Phase 9: managed aws deployment and infrastructure as code"
git push origin main
git tag -a phase-9 -m "Phase 9 complete"
git push origin phase-9
```

## Interview evidence to collect

- An architecture diagram that labels completed and planned components.
- One recorded end-to-end demo and a reproducible local run guide.
- Access-control cases for two synthetic customers.
- Tests for interrupted approvals and repeated action requests.
- An evaluation dataset and real measured results, including limitations.
- Tool traces showing the source of order facts and policy citations.
- A discussion of one failure scenario, how it was diagnosed, and how the design recovers.
- A clear distinction between personal project work and features used in a real production role.
