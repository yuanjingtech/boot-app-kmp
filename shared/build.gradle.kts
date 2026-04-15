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
    androidLibrary {
        namespace = "com.yuanjingtech.boot.app.kmp.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()


//        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
            // api(projects.webview)
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
            api(compose.preview)
            api(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            // JetBrains Compose - version managed by composeMultiplatform plugin
            api(libs.compose.ui)
            api(libs.compose.ui.graphics)
            api(libs.compose.material3)
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            // Preview annotation for commonMain (Compose 1.10.0+)
            api(libs.compose.ui.tooling.preview)
            // Compose resources for font loading
            api(libs.compose.components.resources)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(libs.compose.material3.adaptive)
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
