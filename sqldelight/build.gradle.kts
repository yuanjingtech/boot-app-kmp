import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.publish)
    alias(libs.plugins.sqldelight)
}
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
            implementation(libs.bundles.koin)
            implementation(projects.runblocking)
            api(libs.sqldelight.runtime)
            api(libs.sqldelight.coroutines.extensions)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        sourceSets.androidMain.dependencies {
            api(libs.sqldelight.android.driver)
        }

        // or iosMain, windowsMain, etc.
        sourceSets.nativeMain.dependencies {
            api(libs.sqldelight.native.driver)
        }

        sourceSets.jvmMain.dependencies {
            api(libs.sqldelight.sqlite.driver)
        }
        sourceSets.jsMain.dependencies {
            api(libs.sqldelight.web.worker.driver)
            api(npm("sql.js", "1.6.2"))
//        api("app.cash.sqldelight:sqljs-driver:2.1.0")
            api(devNpm("copy-webpack-plugin", "9.1.0"))
        }
        sourceSets.wasmJsMain.dependencies {
            api(libs.sqldelight.web.worker.driver)
        }
    }
}

android {
    namespace = "com.yuanjingtech.boot.app.kmp.sqldelight"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
sqldelight {
    databases {
        create("BootDatabase") {
            packageName.set("com.yuanjingtech.boot.app.kmp")
            generateAsync.set(true)
        }
    }
}