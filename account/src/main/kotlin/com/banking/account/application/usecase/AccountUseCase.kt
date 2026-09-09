package com.banking.account.application.usecase

import com.banking.account.application.port.input.CreateAccountInput
import com.banking.account.application.port.input.DebitAccountInput
import com.banking.account.application.port.input.GetAccountBalanceInput
import com.banking.account.application.port.output.AccountPersistenceOutput
import com.banking.account.domain.Account
import com.banking.account.domain.exception.AccountNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountUseCase(
    private val accountRepository: AccountPersistenceOutput,
) : CreateAccountInput, DebitAccountInput, GetAccountBalanceInput {

    override fun createAccount(ownerName: String, initialBalanceCents: Long): UUID {
        val account = Account.open(ownerName, initialBalanceCents)
        return accountRepository.save(account).id
    }

    override fun debit(accountId: UUID, amountCents: Long) {
        val account = accountRepository.findById(accountId) ?: throw AccountNotFoundException(accountId)
        account.debit(amountCents)
        accountRepository.save(account)
    }

    override fun getBalanceCents(accountId: UUID): Long {
        val account = accountRepository.findById(accountId) ?: throw AccountNotFoundException(accountId)
        return account.balanceCents
    }
}
