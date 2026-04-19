import dev.whyoleg.sweetspi.gradle.*
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.sweetspi)
}
kotlin {
    withSweetSpi()
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.sample.plugin"
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
            baseName = "shared"
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
            implementation(projects.shared)
            implementation(libs.koin.annotations)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
// sweetspi config start
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