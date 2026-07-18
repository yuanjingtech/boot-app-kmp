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
            implementation(libs.compose.fluent)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
            implementation(libs.backdrop)
        }
        jvmMain.dependencies {
            implementation(libs.backdrop)
        }
        // backdrop is required by iosMain/.../liquidglass/LiquidGlassBackdrop.ios.kt,
        // which references kashif_e.backdrop.* APIs. vibrancy() is intentionally
        // skipped on iOS — backdrop 0.0.1-alpha02 routes through
        // org.jetbrains.skia.ColorMatrix, triggering IrLinkageError.
        iosMain.dependencies {
            implementation(libs.backdrop)
        }
        jsMain.dependencies {
        }
        wasmJsMain.dependencies {
            implementation(libs.backdrop)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmTest.dependencies {
            implementation(libs.compose.test)
        }
    }
}