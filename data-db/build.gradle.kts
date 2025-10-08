plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt") // 先用 kapt，稳定省事
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.harry.phrasebook.data.db"
    compileSdk = 35
    defaultConfig { minSdk = 24 }
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-utils"))
    implementation(project(":domain"))

    // Room
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    // Hilt（带来 javax.inject），同时启用编译器
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")
    // Kotlinx Serialization JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
