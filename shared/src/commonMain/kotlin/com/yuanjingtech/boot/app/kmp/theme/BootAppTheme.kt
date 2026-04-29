package com.yuanjingtech.boot.app.kmp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeMode
import com.yuanjingtech.boot.app.kmp.data.theme.BootThemeStore
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.defaultUiStyle
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun BootAppTheme(
    themeStore: BootThemeStore = koinInject(),
    colorScheme: ColorScheme? = null,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit,
) {
    val themeMode by themeStore.themeModeFlow.collectAsState(initial = BootThemeMode.FOLLOW_SYSTEM)
    val uiStyle by themeStore.uiStyleFlow.collectAsState(initial = defaultUiStyle)
    val isDarkTheme = when (themeMode) {
        BootThemeMode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        BootThemeMode.LIGHT -> false
        BootThemeMode.DARK -> true
    }

    val resolvedColorScheme = colorScheme ?: if (isDarkTheme) darkColorScheme() else lightColorScheme()
    CompositionLocalProvider(
        LocalDarkTheme provides isDarkTheme,
        LocalUiStyle provides uiStyle,
    ) {
        MaterialTheme(
            colorScheme = resolvedColorScheme,
            shapes = shapes,
            typography = LXGWWenKaiTypography(typography),
            content = content
        )
    }
}

