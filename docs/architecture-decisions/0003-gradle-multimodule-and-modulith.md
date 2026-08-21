# 0003 - Gradle multi-módulo combinado com Spring Modulith

## Contexto

O projeto precisa evoluir de monólito modular até serviços extraídos (ledger, statement)
conforme a escalabilidade demandar. É preciso decidir que tipo de fronteira usar entre os
bounded contexts (account, transfer, ledger, statement) desde a Fase 0.

## Decisão

Usar as duas ferramentas em conjunto, cada uma resolvendo uma dimensão diferente:

- **Gradle multi-módulo**: cada bounded context é um módulo Gradle próprio
  (`:account`, `:transfer`, `:ledger`, `:statement`, `:shared-kernel`), com dependências
  explícitas. Um módulo só compila contra outro se a dependência estiver declarada —
  fronteira física, verificada em tempo de compilação.
- **Spring Modulith**: roda dentro do módulo `:app` (que agrega tudo para as Fases 0-3),
  validando que a comunicação entre módulos segue o padrão correto (evento de domínio,
  API pública do módulo) mesmo onde a dependência Gradle existe e permitiria acesso direto.

## Trade-off

- Custo: mais boilerplate de configuração de build desde o início — múltiplos
  `build.gradle.kts`, gestão cuidadosa de `api` vs `implementation` para não vazar tipos
  internos entre módulos.
- Ganho: a extração de um módulo para serviço próprio (Fase 4 — ledger; Fase 5 —
  statement) vira, em grande parte, um exercício de infraestrutura/deploy: trocar
  `implementation(project(":ledger"))` no `:app` por um cliente HTTP/Kafka e subir
  `:ledger` como seu próprio `@SpringBootApplication`.

## Métricas a coletar (na extração, Fase 4+)

- Tempo gasto na extração em si vs tempo gasto corrigindo acoplamento não previsto
- Número de violações de fronteira detectadas pelo Modulith ao longo do projeto
