package com.banking.account.infrastructure.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "account")
class AccountModel(
    @Id
    @Column(name = "account_id")
    val accountId: UUID,

    @Column(name = "owner_name", nullable = false)
    val ownerName: String,

    @Column(name = "balance_cents", nullable = false)
    val balanceCents: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
) {
    protected constructor() : this(UUID.randomUUID(), "", 0, Instant.now())
}