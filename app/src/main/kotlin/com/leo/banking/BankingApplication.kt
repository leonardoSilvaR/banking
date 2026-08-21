package com.leo.banking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulithic

@Modulithic(sharedModules = ["shared-kernel"])
@SpringBootApplication(scanBasePackages = ["com.leo.banking"])
class BankingApplication

fun main(args: Array<String>) {
    runApplication<BankingApplication>(*args)
}
