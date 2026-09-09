package com.banking.account.domain

import com.banking.account.domain.exception.InsufficientBalanceException
import java.time.Instant
import java.util.*

class Account(
    val id: UUID,
    val ownerName: String,
    balanceCents: Long,
    val createdAt: Instant = Instant.now(),
) {
    var balanceCents: Long = balanceCents
        private set

    // Fase 0: read-then-write ingenuo, sem lock — bug proposital (ADR 0001).
    fun debit(amountCents: Long) {
        require(amountCents > 0) { "amountCents deve ser positivo" }
        if (balanceCents < amountCents) {
            throw InsufficientBalanceException(id, amountCents, balanceCents)
        }
        balanceCents -= amountCents
    }

    fun credit(amountCents: Long) {
        require(amountCents > 0) { "amountCents deve ser positivo" }
        balanceCents += amountCents
    }

    companion object {
        fun open(ownerName: String, initialBalanceCents: Long = 0): Account =
            Account(id = UUID.randomUUID(), ownerName = ownerName, balanceCents = initialBalanceCents)
    }
}