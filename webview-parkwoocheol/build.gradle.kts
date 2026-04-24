import dev.whyoleg.sweetspi.gradle.*
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.sweetspi)
    alias(libs.plugins.publish)
}
kotlin {
    withSweetSpi()
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.webview.parkwoocheol"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        withSweetSpi()
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "WebViewParkWoocheol"
            isStatic = true
        }
    }

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
            implementation(libs.bundles.compose)
            implementation(libs.bundles.koin)
            implementation(projects.plugin)
            implementation(projects.webview)
            implementation("io.github.parkwoocheol:compose-webview:1.8.2")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
kotlin {
    sourceSets {
        androidMain {
            // KSP generates META-INF/services/ as resources under this directory,
            // but androidResources does not automatically include it. Register it
            // so the generated service files are packaged into the Android AAR/APK.
            resources.srcDir(layout.buildDirectory.dir("generated/ksp/android/androidMain/resources"))
        }
    }
}