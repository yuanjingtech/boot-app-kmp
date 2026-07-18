import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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
        namespace = "com.yuanjingtech.boot.app.kmp.ad"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
            baseName = "Ad"
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

    // SwiftPM 集成(2026-07-15 唯一保留路径)
    // - 历史背景:Xcode 27 beta clang 触发 `-emit-library` unknown argument;
    //   2026-07-07 临时反向 hack `type: .dynamic → .static` workaround 已删除,
    //   2026-07-09 spm4kmp / CocoaPods POC 全部回退。详见 docs/solutions/runtime-errors/
    // - 当前状态:Xcode 27 GA(27A5218g)+ JetBrains swiftPMDependencies,
    //   iOS app workflow 仍走 xcodebuild 直接构建,convertSyntheticImportProjectIntoDefFile*
    //   失败为已接受代价。
    swiftPMDependencies {
        iosMinimumDeploymentTarget.set("16.0")

        swiftPackage(
            url = "https://github.com/googleads/swift-package-manager-google-mobile-ads",
            version = "11.13.0",
            products = listOf("GoogleMobileAds"),
        )
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.bundles.koin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.admob.android)
        }
    }

    sourceSets.configureEach {
        languageSettings {
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}