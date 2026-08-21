dependencies {
    api(project(":shared-kernel"))
    implementation(project(":account")) // transfer orquestra débito/crédito via API pública de account

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation(kotlin("stdlib"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation(kotlin("test"))
}

// A partir da Fase 2, transfer passa a publicar eventos consumidos por :ledger,
// mas NUNCA deve depender diretamente de :ledger — a comunicação é via evento
// de domínio (shared-kernel) + Spring Modulith event listener, não chamada direta.
