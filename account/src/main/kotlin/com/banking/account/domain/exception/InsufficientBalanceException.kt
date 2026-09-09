package com.banking.account.domain.exception

import java.util.UUID

class InsufficientBalanceException(
    accountId: UUID,
    requestedCents: Long,
    availableCents: Long,
) : RuntimeException(
    "Conta $accountId nao possui saldo suficiente: solicitado=$requestedCents, disponivel=$availableCents"
)
