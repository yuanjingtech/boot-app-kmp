import dev.whyoleg.sweetspi.gradle.*
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.publish)
    alias(libs.plugins.sweetspi)
}
kotlin {
    withSweetSpi()
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.plugin"
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
            baseName = "plugin"
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
            implementation(libs.kotlin.stdlib)
            implementation(libs.bundles.koin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


// app dependencies
kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            // JetBrains Compose - version managed by composeMultiplatform plugin
            api(libs.bundles.compose)
        }
        commonTest.dependencies {
            api(libs.kotlin.test)
        }
        jvmMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.kotlinx.coroutinesSwing)
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling.preview)
}
// sweetspi config start
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