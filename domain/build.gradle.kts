plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
kotlin {
    jvmToolchain(17)
}
dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-utils"))
    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}