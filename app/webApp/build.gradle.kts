import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.publish)
}

kotlin {
    // JVM toolchain (JDK 17) is configured in the root `build.gradle.kts`
    // `subprojects { plugins.withId(...) { ... } }` blocks. The webApp module
    // has no JVM / Android target, so the toolchain hook does not match it;
    // JS and WasmJS targets do not consume the toolchain anyway.

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    // This module has no `commonMain` Kotlin sources — the only entry point
    // lives in `webMain` (shared by both `jsMain` and `wasmJsMain`). The
    // transitive dependencies that `App` needs (`:shared`, Compose runtime,
    // Nav3, Koin, etc.) are already wired by `:app:composeApp`'s `commonMain`,
    // so we only need to depend on `:app:composeApp` itself and add the
    // web-only Navigation3 browser integration on the `webMain` source set.
    sourceSets {
        webMain.dependencies {
            implementation(projects.app.composeApp)
            // `BootSqliteWorker` is the injection point this application has to fill in;
            // `:app:composeApp` keeps `:shared` as an `implementation` dependency, so its
            // API is not on this module's compile classpath unless declared here.
            implementation(projects.shared)
            implementation(libs.bundles.nav3.web)
            // The SQLite worker is this application's responsibility, not the library's:
            // the published klib no longer contains the worker URL, so the specifier has
            // to resolve from the application's own `node_modules`. KGP links the local
            // package but does not install its transitive dependencies, hence the
            // explicit `@sqlite.org/sqlite-wasm` entry below.
            implementation(npm("sqlite-wasm-worker", file("../../sqlite-wasm-worker/worker")))
            implementation(npm("@sqlite.org/sqlite-wasm", "3.50.1-build1"))
            // `ComposeViewport` and `ExperimentalComposeUiApi` live in
            // `org.jetbrains.compose.ui:ui` (re-exported under the
            // `androidx.compose.ui` package on JVM). Even though `:app:composeApp`
            // already pulls the full `compose` bundle into its own
            // `commonMain`, those artifacts are not visible from this
            // module's `webMain` classpath unless declared here.
            implementation(libs.compose.ui)
        }
    }
}
