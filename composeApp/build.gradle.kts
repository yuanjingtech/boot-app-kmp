import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.sqldelight)
}

kotlin {
    android {
        namespace = "com.yuanjingtech.boot.app.kmp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
            implementation(projects.sample.plugin)
            implementation(libs.koin.annotations)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
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
