package com.banking.account.infrastructure.web

import com.banking.account.application.port.input.CreateAccountInput
import com.banking.account.application.port.input.DebitAccountInput
import com.banking.account.application.port.input.GetAccountBalanceInput
import com.banking.account.infrastructure.web.dto.CreateAccountRequest
import com.banking.account.infrastructure.web.dto.CreateAccountResponse
import com.banking.account.infrastructure.web.dto.DebitRequest
import com.banking.account.infrastructure.web.dto.GetBalanceResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val createAccountUseCase: CreateAccountInput,
    private val debitAccountUseCase: DebitAccountInput,
    private val getAccountBalanceInput: GetAccountBalanceInput,
) {

    @PostMapping
    fun create(@RequestBody request: CreateAccountRequest): ResponseEntity<CreateAccountResponse> {
        val accountId = createAccountUseCase.createAccount(request.ownerName, request.initialBalanceCents)
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateAccountResponse(accountId))
    }

    @PostMapping("/{accountId}/debit")
    fun debit(@PathVariable accountId: UUID, @RequestBody request: DebitRequest): ResponseEntity<Void> {
        debitAccountUseCase.debit(accountId, request.amountCents)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{accountId}")
    fun getBalance(@PathVariable accountId: UUID): ResponseEntity<GetBalanceResponse> {
        val balanceCents = getAccountBalanceInput.getBalanceCents(accountId)
        return ResponseEntity.ok(GetBalanceResponse(accountId, balanceCents))
    }
}
