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

    // SwiftPM 集成 — 让 feature/ad 自包含 iOS AdMob 能力
    // 业务模块只需依赖 :feature:ad,iOS app 自动获得 GoogleMobileAds 框架
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

    // Workaround for Xcode 27.0 beta toolchain bug (Kotlin 2.4.0 swiftPMDependencies):
    // The generated Package.swift uses `.library(type: .dynamic)` which invokes
    // `clang -emit-library` — an LLVM linker flag Xcode 27 doesn't recognize.
    // Rewrite to `.static` so xcodebuild uses `-r` (relocatable) instead.
    //
    // NOTE: This workaround causes a secondary "List is empty" error in Kotlin's
    // XcodebuildDefFileWorkAction (it filters ld-args dump files for
    // `@rpath/KotlinMultiplatformLinkedPackageDylib.framework`, which is no
    // longer produced by SPM in static mode). The secondary error means the
    // Gradle task fails even though xcodebuild itself succeeds.
    //
    // When run from xcodebuild (the iOS app workflow), the failure is
    // ignored — the framework is built and signed correctly.
    // When run from Gradle directly, the error is logged but does not
    // affect downstream tasks (the dylib is still produced).
    tasks.matching { it.name.startsWith("convertSyntheticImportProjectIntoDefFile") }.configureEach {
        doFirst {
            val subpackagesDir = layout.buildDirectory.dir("kotlin/swiftImport/subpackages")
                .get().asFile
            if (subpackagesDir.exists()) {
                subpackagesDir.walkTopDown()
                    .filter { it.isFile && it.name == "Package.swift" }
                    .forEach { pkgFile ->
                        val original = pkgFile.readText()
                        val patched = original.replace("type: .dynamic", "type: .static")
                        if (patched != original) {
                            logger.lifecycle("[swiftPM XCode27 workaround] Patched ${pkgFile.relativeTo(projectDir)}")
                            pkgFile.writeText(patched)
                        }
                    }
            }
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}