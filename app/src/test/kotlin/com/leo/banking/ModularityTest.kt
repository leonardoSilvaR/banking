package com.leo.banking

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ModularityTest {

    @Test
    fun `verifica que os modulos respeitam as fronteiras declaradas`() {
        val modules = ApplicationModules.of(BankingApplication::class.java)
        modules.verify()
    }

    @Test
    fun `imprime a documentacao dos modulos`() {
        val modules = ApplicationModules.of(BankingApplication::class.java)
        modules.forEach { println(it) }
    }
}
