package com.banking.account.application.port.output

import com.banking.account.domain.Account
import java.util.UUID

interface AccountPersistenceOutput {
    fun save(account: Account): Account
    fun findById(accountId: UUID): Account?
}
