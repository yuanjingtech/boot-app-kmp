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
            // `worker/worker.js` does `import sqlite3InitModule from '@sqlite.org/sqlite-wasm'`.
            // Declaring the dependency here (and not only in `worker/package.json`) makes npm
            // hoist it into the workspace root, where webpack can resolve it while bundling the
            // worker chunk. Relying on the linked package's own `dependencies` is not enough:
            // npm links `worker/` but does not install its transitive dependencies.
            implementation(npm("@sqlite.org/sqlite-wasm", "3.50.1-build1"))
        }
    }
}