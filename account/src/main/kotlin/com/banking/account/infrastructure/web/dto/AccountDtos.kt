package com.banking.account.infrastructure.web.dto

import java.util.UUID

data class CreateAccountRequest(val ownerName: String, val initialBalanceCents: Long)

data class CreateAccountResponse(val accountId: UUID)

data class DebitRequest(val amountCents: Long)

data class GetBalanceResponse(val accountId: UUID, val balanceCents: Long)
