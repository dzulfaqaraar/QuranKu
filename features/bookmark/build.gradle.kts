plugins {
    alias(libs.plugins.android.dynamic.feature)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.dzulfaqar.quranku.bookmark"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":core"))

    // ==================================================
    // Shared Dependencies
    // ==================================================

    implementation(libs.bundles.common.android)

    // Hilt Dependency Injection
    implementation(libs.dagger.android)
    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)

    // ViewBinding - using native Android ViewBinding (no external library needed)

    // ==================================================
    // Testing Dependencies
    // ==================================================

    testImplementation(libs.bundles.common.testing)
    testImplementation(libs.bundles.ui.testing)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}