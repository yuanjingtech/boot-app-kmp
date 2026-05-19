import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.publish)
}
kotlin {
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

//        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
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
            implementation(libs.bundles.koin)
            implementation(libs.bundles.compose)
            implementation(libs.bundles.coil)
            implementation(libs.compose.material.icons.extended)
            implementation(projects.network)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
            implementation(libs.backdrop)
            implementation(libs.compose.fluent.android)
        }
        jvmMain.dependencies {
            implementation(libs.backdrop)
        }
        iosArm64Main.dependencies {
            implementation(libs.backdrop)
        }
        iosSimulatorArm64Main.dependencies {
            implementation(libs.backdrop)
        }
        jsMain.dependencies {
        }
        wasmJsMain.dependencies {
            // fluent-wasm-js artifact not available, wasmJsMain uses stub implementations
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}