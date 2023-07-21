@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin)
    id("jacoco")
}

dependencies {
    testImplementation(libs.bundles.junit4)
}

tasks.withType(Test::class.java).configureEach {
//    allJvmArgs = allJvmArgs + listOf("-Xmx4192M")
}

tasks.named("jacocoTestReport").configure {
    dependsOn(tasks.withType<Test>())
}
