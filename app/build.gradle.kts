@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("jacoco")
    alias(libs.plugins.androidCacheFix)
}

android {
    namespace = "com.jacocoissueapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.jacocoissueapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions.unitTests.all {
        // this line breaks the cache for jacoco
        it.allJvmArgs = it.allJvmArgs + listOf("-Xmx4192M")
        it.configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.bundles.junit5)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

tasks.withType(Test::class.java).configureEach {
    useJUnitPlatform()
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn(tasks.withType<Test>()) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    val javaDebugTree = project.fileTree(
        mapOf("dir" to "${project.buildDir}/intermediates/javac/release"),
    )
    val kotlinDebugTree = project.fileTree(
        mapOf("dir" to "${project.buildDir}/tmp/kotlin-classes/release"),
    )
    val kotlinModuleTree = project.fileTree(
        mapOf("dir" to "${project.buildDir}/classes/kotlin/main"),
    )

    // Source dir is sometimes named "java", sometimes "kotlin"
    sourceDirectories.from("${project.projectDir}/src/main/java")

    // We need to target both java and kotlin build folder
    classDirectories.from(javaDebugTree, kotlinDebugTree, kotlinModuleTree)
    executionData(
        fileTree(
            mapOf(
                "dir" to buildDir,
                "includes" to listOf(
                    "jacoco/testReleaseUnitTest.exec",
                    "jacoco/test.exec",
                    "outputs/code-coverage/connected/*coverage.ec",
                ),
            ),
        ),
    )
}
