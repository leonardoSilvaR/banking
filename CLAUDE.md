# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

A learning project about scaling financial systems: an incremental evolution from a
modular monolith to event sourcing, CQRS, and Saga — with real measurement of
concurrency, latency, and throughput at each phase. Every architectural decision is
recorded as an ADR in `docs/architecture-decisions/`, and every phase should get
measured results in `docs/benchmarks/results.md`.

**Current state**: the project is at Phase 0 (baseline) and largely scaffolding —
Gradle modules, DB migrations, and one intentionally-failing concurrency test exist,
but `account`/`transfer` application code (services, controllers, entities) has not
been implemented yet.

## Commands

```bash
./gradlew build                 # build + run all tests across modules
./gradlew test                  # run all tests
./gradlew :account:test         # run tests for a single module
./gradlew :app:bootRun          # run the application
```

Run a single test class/method (standard Gradle JUnit filter):

```bash
./gradlew :account:test --tests "com.leo.banking.account.ConcurrentDebitTest"
```

Observability stack + load tests (k6), used to actually measure each phase:

```bash
docker compose -f load-tests/k6/docker-compose.yml up -d
k6 run load-tests/k6/concurrent-same-account.js
k6 run load-tests/k6/throughput-baseline.js
```

## Architecture

### Bounded contexts as Gradle modules

Each bounded context is a separate Gradle module with a **physically enforced**
dependency graph (declared in each `build.gradle.kts`), not just a package
convention:

- `shared-kernel` — value types (`Money`, `AccountId`) and shared domain events.
  Depends on nothing else in the project.
- `account` — owns the account and materialized balance. Depends only on
  `shared-kernel`. Must **never** depend on `transfer`, `ledger`, or `statement` —
  those must consume `account` only through its public application-layer API, never
  its repository/entity internals.
- `transfer` — orchestrates transfers between accounts by calling `account`'s public
  API. From Phase 2 on it publishes domain events consumed by `ledger`, but must
  **never** depend directly on `:ledger` — cross-context communication goes through
  domain events (types from `shared-kernel`) + Spring Modulith event listeners, not
  direct calls.
- `ledger` — append-only immutable event log (arrives in Phase 2). Depends only on
  `shared-kernel`, not on `account`/`transfer`. This near-zero coupling is why it's
  the first module planned for physical extraction (Phase 4).
- `statement` — read-model/dashboard (arrives in Phase 5 via CQRS). Purely read-side:
  consumes events (from `ledger`, via Kafka from Phase 4/5 on) and maintains a query
  projection. No compile-time dependency on `account`, `transfer`, or `ledger`.
- `app` — the only module that aggregates every bounded context into one deployable
  process. From Phase 4 on, as `ledger` gets extracted into its own service, `app`
  stops depending on it directly and talks to it over Kafka/HTTP instead.

Two mechanisms enforce these boundaries, deliberately at different times:

- **Gradle multi-module** enforces the boundary at **compile time** — a module can
  only see another if the dependency is declared in `build.gradle.kts`. This is what
  makes extracting a module into its own service (Phase 4+) an infrastructure
  exercise rather than an untangling exercise.
- **Spring Modulith** (wired in `app/src/main/kotlin/.../BankingApplication.kt` via
  `@Modulithic`, verified by `app/src/test/kotlin/.../ModularityTest.kt`) enforces the
  boundary at **test time** — it fails the build if a module reaches into another
  module's internals even where the Gradle dependency would technically allow it.

When adding code to a module, check that module's `build.gradle.kts` — the comments
in each one state which other modules it is and isn't allowed to depend on, and why.

### Phased evolution (branch per phase)

The project evolves through phases, historically one branch per phase (see README for
the full table: `phase-0-monolith-baseline` through `phase-6-saga-cross-context`).
Each phase changes a specific architectural aspect (locking strategy, ledger
extraction, CQRS, sharding, sagas) and each has a corresponding ADR. Only `main`
exists currently. Before implementing a phase's feature, read its ADR in
`docs/architecture-decisions/` for the intended design and the trade-offs already
considered — don't re-derive decisions that are already documented there.

Money is always represented in integer cents (`balanceCents`, `amountCents`), never
floating point — this convention is set in the Phase 0 migration
(`account/src/main/resources/db/migration/V1__phase0_baseline.sql`) and should be
followed everywhere balances/amounts appear.

### Concurrency tests are intentional documentation, not flaky tests

`account/src/test/kotlin/com/leo/banking/account/ConcurrentDebitTest.kt` is written to
**fail on purpose** at Phase 0 — it documents the lost-update bug before Phase 1's
locking fix makes it pass. If you find a concurrency test failing, check which phase
the code is meant to represent before treating it as a regression.
