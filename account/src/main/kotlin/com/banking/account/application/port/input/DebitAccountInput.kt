package com.banking.account.application.port.input

import java.util.UUID

interface DebitAccountInput {
    fun debit(accountId: UUID, amountCents: Long)
}
