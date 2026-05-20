package com.yuanjingtech.boot.app.kmp.ui.fluent

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.luminance

val LocalFluentColors = compositionLocalOf { FluentColors.Dark }

@Composable
fun FluentTheme(
    content: @Composable () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val isLight = scheme.surface.luminance() > 0.5f
    val colors = if (isLight) FluentColors.Light else FluentColors.Dark
    CompositionLocalProvider(LocalFluentColors provides colors) {
        content()
    }
}
