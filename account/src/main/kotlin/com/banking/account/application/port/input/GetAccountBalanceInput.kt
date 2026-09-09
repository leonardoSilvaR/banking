package com.banking.account.application.port.input

import java.util.UUID

interface GetAccountBalanceInput {
    fun getBalanceCents(accountId: UUID): Long
}
