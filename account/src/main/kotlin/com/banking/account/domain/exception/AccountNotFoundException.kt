package com.banking.account.domain.exception

import java.util.UUID

class AccountNotFoundException(accountId: UUID) : RuntimeException("Conta nao encontrada: $accountId")
