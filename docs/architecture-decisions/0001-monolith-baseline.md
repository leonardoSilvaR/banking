# 0001 - Monólito modular como baseline

## Contexto

Precisamos de um ponto de partida que reproduza, de forma controlada e mensurável, o
problema clássico de concorrência em sistemas financeiros: lost update no saldo de conta
sob escritas concorrentes (read-then-write sem proteção).

## Decisão

Fase 0 implementa `account.debit()` de forma propositalmente ingênua — sem lock, sem
versionamento — para servir de baseline de comparação com as fases seguintes.

```kotlin
fun debit(accountId: UUID, amount: Long) {
    val account = accountRepository.findById(accountId)
    if (account.balanceCents < amount) throw InsufficientBalanceException()
    account.balanceCents -= amount
    accountRepository.save(account)
}
```

## Trade-off

- Vantagem: expõe o bug de forma reproduzível via teste de concorrência, servindo de
  contraste quantitativo para as estratégias de lock da Fase 1.
- Custo: este código não deve ir para nenhum ambiente real — existe apenas como
  documentação viva do problema.

## Métricas a coletar

- Saldo final esperado vs saldo final observado sob N threads concorrentes
- Quantidade de updates "perdidos" (lost updates) em função do número de threads
