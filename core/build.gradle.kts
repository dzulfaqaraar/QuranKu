plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.dzulfaqar.quranku.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        buildConfigField("String", "BASE_URL", "\"api.quran.com\"")
        buildConfigField("String", "AUDIO_URL", "\"https://cdn.islamic.network/quran/audio/\"")
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
    
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
    
    ndkVersion = "25.2.9519653"
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {
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
    // Core Module Only
    // ==================================================

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    
    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)

    // Networking
    api(libs.retrofit)
    implementation(libs.bundles.networking)

    implementation(libs.chucker)
    api(libs.timber)
    
    // Testing
    testImplementation(libs.bundles.common.testing)
    testImplementation(libs.bundles.ui.testing)
    testApi(libs.bundles.common.testing)
    androidTestImplementation(libs.androidx.room.testing)
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}