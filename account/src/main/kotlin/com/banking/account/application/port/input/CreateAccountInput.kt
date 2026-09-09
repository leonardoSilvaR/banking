package com.banking.account.application.port.input

import java.util.UUID

interface CreateAccountInput {
    fun createAccount(ownerName: String, initialBalanceCents: Long): UUID
}
