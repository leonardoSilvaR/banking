dependencies {
    api(project(":shared-kernel"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation(kotlin("stdlib"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation(kotlin("test"))
}

// statement é puramente read-side: consome eventos (de :ledger, via Kafka a
// partir da Fase 4/5) e mantém uma projeção otimizada para consulta de extrato.
// Não depende de :account, :transfer ou :ledger em tempo de compilação.
