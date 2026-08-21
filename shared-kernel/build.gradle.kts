dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

// shared-kernel não depende de nenhum outro módulo do projeto.
// Contém apenas tipos de valor (Money, AccountId) e eventos de domínio
// compartilhados entre os bounded contexts.
