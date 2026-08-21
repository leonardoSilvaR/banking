# Benchmarks consolidados por fase

Preencher conforme cada fase for medida com k6 + Prometheus/Grafana.

| Fase | p50 (ms) | p99 (ms) | Throughput máx (req/s) | Observações |
|---|---|---|---|---|
| Fase 0 (baseline, sem lock) | | | | Lost update esperado — não é métrica de performance válida |
| Fase 1 (lock pessimista) | | | | |
| Fase 1 (lock otimista) | | | | |
| Fase 2 (ledger local) | | | | Overhead de escrever 2 tabelas |
| Fase 3 (sharding lógico) | | | | Contenção vs nº de partições |
| Fase 4 (ledger extraído + Kafka) | | | | Latência de rede/Kafka adicionada |
| Fase 5 (CQRS statement) | | | | Lag de consumer (consistência eventual medida) |
| Fase 6 (saga cross-context) | | | | Tempo de convergência sob falha injetada |
