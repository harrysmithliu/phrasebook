plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation(project(":core-model"))
}