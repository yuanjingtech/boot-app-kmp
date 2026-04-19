import dev.whyoleg.sweetspi.gradle.*
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.sweetspi)
}

kotlin {
    withSweetSpi()
    android {
        namespace = "com.yuanjingtech.boot.app.kmp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        withSweetSpi()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.webview)
            implementation(projects.webviewParkwoocheol)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.koin.annotations)
            implementation(libs.bundles.nav3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        webMain.dependencies {
            implementation(libs.bundles.nav3.web)
        }
    }
}
sqldelight {
    databases {
        create("Database") {
            packageName.set("com.yuanjingtech.boot.app.kmp")
            generateAsync.set(true)
        }
    }
}
koinCompiler {
    compileSafety = false
    userLogs = true
    debugLogs = true
}
kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.sweetspi.runtime.jvm)
            }
            // KSP generates META-INF/services/ as resources under this directory,
            // but androidResources does not automatically include it. Register it
            // so the generated service files are packaged into the Android AAR/APK.
            resources.srcDir(layout.buildDirectory.dir("generated/ksp/android/androidMain/resources"))
        }
    }
}
dependencies {
    add("kspAndroid", libs.sweetspi.processor)
}