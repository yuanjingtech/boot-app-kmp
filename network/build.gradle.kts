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
    namespace = "com.yuanjingtech.boot.app.kmp.network"
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
            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor.client)
        }
        //jvm
        jvmMain.dependencies {
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        //web
        webMain.dependencies {

        }
        jsMain.dependencies {
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.wasm)
        }
        //native
        nativeMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        iosArm64Main.dependencies {
        }
        iosSimulatorArm64Main.dependencies {
        }
    }
}
