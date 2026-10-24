import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.publish)
}
kotlin {
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.webview"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
            implementation(libs.bundles.compose)
            implementation(libs.bundles.koin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            // ⚠️ ui-test-junit4 只有 JVM/Android 有，JS/WASM 没有
            // ⇒ 「前进/后退工具栏」的测试是 Compose UI 测试 ⇒ 只能放 jvmTest
            implementation(libs.compose.test)
            // ⚠️ compose.ui.test 只依赖 skiko-awt（不含原生 dylib）⇒ 跑 runComposeUiTest 会报
            //    "Cannot find libskiko-macos-arm64.dylib.sha256" ⇒ 必须显式带上带原生库的
            //    skiko-awt-runtime-<os>；compose.desktop.currentOs 会传递引入它（与 :plugin / :shared 同源做法）
            implementation(compose.desktop.currentOs)
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}