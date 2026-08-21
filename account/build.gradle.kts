dependencies {
    api(project(":shared-kernel")) // tipos de valor usados na API pública do módulo

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation(kotlin("stdlib"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation(kotlin("test"))
}

// account NÃO deve depender de :transfer, :ledger ou :statement.
// Se algum desses precisar de dados de account, deve consumir a API pública
// deste módulo (application layer), nunca acessar repository/entity internos.
