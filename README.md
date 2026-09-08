# banking

Projeto de aprendizado prático sobre escalabilidade de sistemas financeiros: evolução
incremental de um monólito modular até event sourcing, CQRS e Saga — com medição real
de concorrência, latência e throughput em cada fase.

## Domínio

Bounded contexts, desde o início, mesmo rodando no mesmo processo nas fases iniciais:

- **account** — dono da conta e saldo materializado
- **transfer** — orquestra transferências entre contas
- **ledger** — log de eventos imutável (nasce na Fase 2)
- **statement** — extrato/dashboard, read-model (nasce na Fase 5, via CQRS)
- **shared-kernel** — tipos de valor (Money, AccountId) e eventos de domínio compartilhados

## Por que Gradle multi-módulo + Spring Modulith

- **Gradle multi-módulo** garante fronteira física em tempo de **compilação**: um módulo
  só enxerga outro se a dependência estiver declarada no `build.gradle.kts`. Isso torna a
  extração de um módulo para serviço próprio (Fase 4+) um exercício de infraestrutura,
  não de "descobrir acoplamento escondido".
- **Spring Modulith** garante fronteira lógica em tempo de **teste**: valida que um módulo
  não acessa classe interna de outro sem passar pela API pública, detecta ciclos, e gera
  diagrama C4 automaticamente.

As duas coisas são complementares, não concorrentes.

## Fases (uma branch por fase)

| Branch | O que muda | O que medir |
|---|---|---|
| `phase-0-monolith-baseline` | Update direto no saldo, sem lock (bug proposital) | Prova de lost update sob concorrência |
| `phase-1-locking-strategies` | Lock pessimista (`FOR UPDATE`) vs otimista (version) | Throughput/latência comparados |
| `phase-2-ledger-context` | Ledger append-only + saldo materializado na mesma transação | Overhead de escrever 2 tabelas vs 1 |
| `phase-3-logical-sharding` | Particiona `account_id` em N tabelas/schemas por hash | Contenção vs número de partições |
| `phase-4-ledger-extraction` | Ledger vira serviço próprio + Kafka (outbox pattern) | Latência adicionada pela rede/Kafka |
| `phase-5-cqrs-statement-context` | Statement vira read-model via Kafka consumer | Lag real entre escrita e leitura |
| `phase-6-saga-cross-context` | Saga para transferência entre contas em contexts/serviços diferentes, com falha injetada | Tempo de convergência + taxa de sucesso de compensação |

## Como rodar

```bash
./gradlew build
docker compose -f config/docker-compose.yml up -d
./gradlew :app:bootRun
```

Testes de carga (k6):
```bash
k6 run load-tests/k6/concurrent-same-account.js
k6 run load-tests/k6/throughput-baseline.js
```

## ADRs

Cada decisão relevante vira um ADR em `docs/architecture-decisions/`, com contexto,
decisão, trade-off e (quando aplicável) as métricas medidas. Ver `docs/benchmarks/results.md`
para a tabela consolidada por fase.
