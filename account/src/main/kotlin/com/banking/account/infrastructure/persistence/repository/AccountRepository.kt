package com.banking.account.infrastructure.persistence.repository

import com.banking.account.infrastructure.persistence.model.AccountModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<AccountModel, UUID>