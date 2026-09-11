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
            implementation(libs.bundles.nav3.web)
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
