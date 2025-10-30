import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.publish)
}
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.yuanjingtech.boot.app.kmp.logging"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
// logging
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.logging)
        }
        //jvm
        jvmMain.dependencies {
            implementation(libs.kotlin.logging.jvm)
        }
        androidMain.dependencies {
            // 使用通用的 kotlin-logging 而不是特定的 android 变体
            implementation(libs.kotlin.logging)
        }
        //web
        webMain.dependencies {

        }
        jsMain.dependencies {
            implementation(libs.kotlin.logging.js)
        }
        wasmJsMain.dependencies {
            implementation(libs.kotlin.logging.wasm.js)
        }
        //native
        nativeMain.dependencies {

        }
        iosMain.dependencies {
        }
        iosArm64Main.dependencies {
            implementation(libs.kotlin.logging.iosarm64)
        }
        iosSimulatorArm64Main.dependencies {
            implementation(libs.kotlin.logging.iossimulatorarm64)
        }
    }
}

// 强制统一 kotlin-logging 版本以避免冲突
configurations.all {
    resolutionStrategy {
        force("io.github.oshai:kotlin-logging:7.0.7")
        force("io.github.oshai:kotlin-logging-jvm:7.0.7")
        force("io.github.oshai:kotlin-logging-js:7.0.7")
    }
}