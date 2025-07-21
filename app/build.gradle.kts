plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.dzulfaqar.quranku"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dzulfaqar.quranku"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    dynamicFeatures += setOf(":features:setting", ":features:bookmark", ":features:murattal")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":player"))

    // Play Feature Delivery for Dynamic Features
    implementation(libs.play.feature.delivery)

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
    // App Module Only
    // ==================================================

    // Navigation
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity)

    debugImplementation(libs.leakcanary)
    api(libs.shimmer)
    api(libs.lottie)

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