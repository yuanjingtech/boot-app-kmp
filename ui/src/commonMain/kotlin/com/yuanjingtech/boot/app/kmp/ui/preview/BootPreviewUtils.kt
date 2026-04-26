package com.yuanjingtech.boot.app.kmp.ui.components

import androidx.compose.ui.tooling.preview.Preview

/**
 * Preview at both UI styles (light mode).
 */
@Preview(name = "LiquidGlass", group = "UI Style")
@Preview(name = "Material3", group = "UI Style")
annotation class StylePreviews

/**
 * Preview at light and dark mode.
 */
@Preview(name = "Light", group = "Night Mode")
@Preview(name = "Dark", group = "Night Mode")
annotation class UiModePreviews

/**
 * All 4 combinations: 2 styles × 2 modes.
 */
@Preview(name = "LiquidGlass Light", group = "Style+Mode")
@Preview(name = "LiquidGlass Dark", group = "Style+Mode")
@Preview(name = "Material3 Light", group = "Style+Mode")
@Preview(name = "Material3 Dark", group = "Style+Mode")
annotation class BootPreviews
