package com.banking.transfer.application.usecase

import com.banking.account.infrastructure.persistence.repository.AccountRepository

class TransferUseCase(
    val accountRepository: AccountRepository,
) {
}