import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.publish)
}
kotlin {
    android {
        namespace = "com.yuanjingtech.boot.app.kmp.shared"
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
            api(libs.kotlin.stdlib)
            // put your Multiplatform dependencies here
            api(libs.bundles.koin)
            api(projects.runblocking)
            api(projects.logging)
            api(projects.sqldelight)
            api(projects.subapp)
            api(projects.network)
            api(projects.ui)
            api(projects.webview)
            api(libs.kotlinx.datetime)
            api(libs.compose.material.icons.extended)
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