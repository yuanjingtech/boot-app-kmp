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
    alias(libs.plugins.room3)
}
kotlin {
    withSweetSpi()
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.shared"
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
            linkerOpts.add("-lsqlite3")
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
            api(libs.kotlin.stdlib)
            // put your Multiplatform dependencies here
            api(libs.bundles.koin)
            api(projects.runblocking)
            api(projects.plugin)
            api(projects.logging)
            api(projects.sqldelight)
            api(projects.subapp)
            api(projects.network)
            api(projects.ui)
            api(projects.webview)
            implementation(projects.webviewParkwoocheol)
            api(libs.kotlinx.datetime)
            api(libs.compose.material.icons.extended)
            api(libs.room3.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            // Note: ui-test-junit4 is only available for JVM/Android, not JS/WASM
            // Use it in platform-specific test source sets
        }
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}
// app dependencies
kotlin {
    sourceSets {
        commonMain.dependencies {
            // JetBrains Compose - version managed by composeMultiplatform plugin
            api(libs.bundles.compose)
        }
        commonTest.dependencies {
            api(libs.kotlin.test)
        }
        androidMain.dependencies {
            api(libs.androidx.activity.compose)
        }
        jvmMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.kotlinx.coroutinesSwing)
        }
        jvmTest.dependencies {
            api(libs.compose.test)
        }
    }
}
kotlin {
    sourceSets {
        nativeMain.dependencies {
            implementation(libs.room3.runtime)
            implementation(libs.sqlite.bundled)
        }
        androidMain.dependencies {
            implementation(libs.room3.runtime)
            implementation(libs.sqlite.bundled)
        }
        jvmMain.dependencies {
            implementation(libs.room3.runtime)
            implementation(libs.sqlite.bundled)
        }
        webMain.dependencies {
            implementation(libs.room3.runtime)
            implementation(libs.sqlite.web)
            implementation(npm("sqlite-wasm-worker", file("../sqlite-wasm-worker/worker")))
        }
    }
}
dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling.preview)
}
//room3 KSP
dependencies {
    // KSP generates actual BootDatabaseConstructor for all platforms
    add("kspAndroid", libs.room3.compiler)
    add("kspJvm", libs.room3.compiler)
    add("kspIosSimulatorArm64", libs.room3.compiler)
    add("kspIosArm64", libs.room3.compiler)
    add("kspJs", libs.room3.compiler)
    add("kspWasmJs", libs.room3.compiler)
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