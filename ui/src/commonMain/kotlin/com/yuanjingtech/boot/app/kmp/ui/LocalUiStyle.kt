package com.yuanjingtech.boot.app.kmp.ui

import androidx.compose.runtime.compositionLocalOf

/**
 * Provides the current UI style (LiquidGlass or Material3) throughout the composition tree.
 * The value is driven by [BootThemeStore.uiStyleFlow] in [BootAppTheme].
 * The default (used when no provider is in scope) is [defaultUiStyle], which is platform-specific.
 */
val LocalUiStyle = compositionLocalOf { defaultUiStyle }
