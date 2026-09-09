plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":shared-kernel"))
    implementation(project(":account"))
    implementation(project(":transfer"))
    implementation(project(":ledger"))
    implementation(project(":statement"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testImplementation(kotlin("test"))
}

// :app é o único módulo que agrega todos os bounded contexts num único processo.
// A partir da Fase 4, quando :ledger for extraído, ele deixa de aparecer aqui
// como dependência direta e passa a ser um serviço externo consumido via Kafka.
