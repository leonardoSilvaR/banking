dependencies {
    api(project(":shared-kernel"))

//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.postgresql:postgresql")
    implementation(kotlin("stdlib"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation(kotlin("test"))
}

// ledger não depende de :account nem de :transfer — apenas consome eventos de
// domínio (definidos em :shared-kernel). É o módulo com menos acoplamento
// bidirecional, por isso é o primeiro candidato à extração física (Fase 4).
