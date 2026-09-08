package com.banking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulithic

@Modulithic(sharedModules = ["account"])
@SpringBootApplication(scanBasePackages = ["com.banking"])
class BankingApplication

fun main(args: Array<String>) {
    runApplication<BankingApplication>(*args)
}
