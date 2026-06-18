import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    // Do NOT apply kotlin-android — AGP 9.0 includes built-in Kotlin support
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.yuanjingtech.boot.app.kmp.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.yuanjingtech.boot.app.kmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        // Google AdMob App ID(对应 AndroidManifest.xml 中 com.google.android.gms.ads.APPLICATION_ID)
        // release 构建应替换为生产 ID
        manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.shared)
    implementation(projects.app.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.components.resources)
    debugImplementation(libs.compose.ui.tooling)
}
