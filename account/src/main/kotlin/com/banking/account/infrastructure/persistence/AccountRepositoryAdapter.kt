package com.banking.account.infrastructure.persistence

import com.banking.account.application.port.output.AccountPersistenceOutput
import com.banking.account.domain.Account
import com.banking.account.infrastructure.persistence.model.AccountModel
import com.banking.account.infrastructure.persistence.repository.AccountRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepositoryAdapter(
    private val repository: AccountRepository,
) : AccountPersistenceOutput {

    override fun save(account: Account): Account {
        val entity = AccountModel(
            accountId = account.id,
            ownerName = account.ownerName,
            balanceCents = account.balanceCents,
            createdAt = account.createdAt,
        )
        repository.save(entity)
        return account
    }

    override fun findById(accountId: UUID): Account? =
        repository.findById(accountId).map { it.toDomain() }.orElse(null)

    private fun AccountModel.toDomain(): Account =
        Account(id = accountId, ownerName = ownerName, balanceCents = balanceCents, createdAt = createdAt)
}
