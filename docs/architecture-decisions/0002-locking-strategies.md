# 0002 - Estratégias de controle de concorrência

## Contexto

A Fase 0 provou o lost update. Precisamos corrigir isso e comparar duas estratégias
clássicas de controle de concorrência em banco relacional.

## Decisão

Implementar as duas estratégias em branches/paths separados para comparação direta sob
a mesma carga de teste:

- **Lock pessimista**: `SELECT ... FOR UPDATE` — serializa acesso à linha da conta.
- **Lock otimista**: coluna `version`, `UPDATE ... WHERE version = X`, retry em conflito
  (`OptimisticLockException`).

## Trade-off

- Pessimista: previsível sob alta contenção (mesma conta disputada por muitas threads),
  mas threads ficam bloqueadas esperando o lock, aumentando latência de cauda (p99).
- Otimista: melhor throughput sob baixa contenção (contas diferentes), mas sob alta
  contenção gera muitos retries, o que pode custar mais que o lock pessimista.

## Métricas a coletar

- Latência p50/p99 de cada estratégia, sob os mesmos cenários de carga
- Taxa de retry (otimista) vs tempo médio de espera de lock (pessimista)
- Throughput máximo antes de degradação, para cada estratégia
