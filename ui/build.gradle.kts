import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.publish)
}
kotlin {
    androidLibrary {
        namespace = "com.yuanjingtech.boot.app.kmp.ui"
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
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.coil)
            implementation(projects.network)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.bundles.coil.android)
        }
    }
}


// logging
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.koin)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.material3.adaptive)
        }
        //jvm
        jvmMain.dependencies {
        }
        androidMain.dependencies {
        }
        //web
        webMain.dependencies {

        }
        jsMain.dependencies {
        }
        wasmJsMain.dependencies {
        }
        //native
        nativeMain.dependencies {
        }
        iosMain.dependencies {
        }
        iosArm64Main.dependencies {
        }
        iosSimulatorArm64Main.dependencies {
        }
    }
}
