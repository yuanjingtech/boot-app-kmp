package com.yuanjingtech.boot.app.kmp.webapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.yuanjingtech.boot.app.kmp.App

/**
 * Web entry point for the `:app:webApp` wrapper module.
 *
 * The same compiled source is reused by both the JS and WasmJS browser
 * targets (both produce an executable bundled by webpack). The actual UI
 * lives in `:app:composeApp` and is shared across every other platform
 * wrapper (Android, Desktop, iOS).
 *
 * This file lives in `webMain`, the intermediate source set that the
 * Kotlin Multiplatform plugin automatically wires to both `jsMain` and
 * `wasmJsMain` whenever both targets are present in a module.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App()
    }
}
