package com.banking.account

//import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Teste de invariante sob concorrência, não teste de carga genérico.
 *
 * Fase 0: este teste FALHA de propósito — documenta o bug de lost update
 * antes da correção via locking (Fase 1).
 *
 * Fase 1+: este mesmo teste deve passar, validando que a estratégia de
 * lock (pessimista ou otimista) elimina o lost update.
 */
class ConcurrentDebitTest {


    @Test
    fun `say my name`() {
        val b1 = BigDecimal.ONE
        val b2 = BigDecimal.ONE
        Assertions.assertTrue(b1.compareTo(b2) == 0);
    }

    // Substituir por injeção real do repository/service quando os módulos
    // account/transfer estiverem implementados.
//    private lateinit var transferService: TransferServiceUnderTest
//
//    @Test
//    fun `saldo nunca fica negativo sob debitos concorrentes na mesma conta`() {
//        val accountId = transferService.createAccountWithBalance(10_000) // R$ 100,00
//        val numThreads = 20
//        val debitAmount = 1_000L // R$ 10,00 cada
//        val executor = Executors.newFixedThreadPool(numThreads)
//        val latch = CountDownLatch(numThreads)
//        val successCount = AtomicInteger(0)
//
//        repeat(numThreads) {
//            executor.submit {
//                try {
//                    transferService.debit(accountId, debitAmount)
//                    successCount.incrementAndGet()
//                } catch (e: InsufficientBalanceException) {
//                    // esperado após o saldo se esgotar
//                } finally {
//                    latch.countDown()
//                }
//            }
//        }
//
//        latch.await(10, TimeUnit.SECONDS)
//        executor.shutdown()
//
//        val finalBalance = transferService.currentBalance(accountId)
//
//        // Só cabem 10 débitos de R$10 em R$100.
//        Assertions.assertTrue(successCount.get().compareTo(10) == 0)
//        Assertions.assertTrue(finalBalance.compareTo(0) == 0)
////        assertThat(successCount.get()).isEqualTo(10)
////        assertThat(finalBalance).isEqualTo(0)
//    }
}

// Placeholders — trocar pela implementação real de :account / :transfer.
interface TransferServiceUnderTest {
    fun createAccountWithBalance(balanceCents: Long): UUID
    fun debit(accountId: UUID, amountCents: Long)
    fun currentBalance(accountId: UUID): Long
}

class InsufficientBalanceException : RuntimeException()
