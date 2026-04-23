import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    js { browser { useEsModules() } }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser { useEsModules() } }

    sourceSets {
        commonMain.dependencies {
            api(libs.sqlite.web)
            implementation(npm("sqlite-wasm-worker", file("worker")))
        }
    }
}