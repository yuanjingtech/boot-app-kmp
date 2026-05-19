package com.yuanjingtech.boot.app.kmp.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.yuanjingtech.boot.app.kmp.ui.BootUiStyle
import com.yuanjingtech.boot.app.kmp.ui.LocalUiStyle
import com.yuanjingtech.boot.app.kmp.ui.fluent.FluentTheme
import com.yuanjingtech.boot.app.kmp.ui.liquidglass.LiquidGlassTheme

// ─── Preview Color Schemes ────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    surface = Color(0xFF1C1B1F),
)

private val LightColorScheme = lightColorScheme(
    surface = Color(0xFFFFFBFE),
)

// ─── PreviewWrapperProvider Implementations (compose 1.11+) ────────────────────

class LiquidGlassPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            MaterialTheme(colorScheme = DarkColorScheme) {
                LiquidGlassTheme {
                    content()
                }
            }
        }
    }
}

class LiquidGlassLightPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            MaterialTheme(colorScheme = LightColorScheme) {
                LiquidGlassTheme {
                    content()
                }
            }
        }
    }
}

class Material3PreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.MATERIAL3) {
            MaterialTheme {
                content()
            }
        }
    }
}

class FluentPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.FLUENT) {
            MaterialTheme(colorScheme = DarkColorScheme) {
                FluentTheme {
                    content()
                }
            }
        }
    }
}

class FluentLightPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.FLUENT) {
            MaterialTheme(colorScheme = LightColorScheme) {
                FluentTheme {
                    content()
                }
            }
        }
    }
}

// ─── BootPreviewUtils ──────────────────────────────────────────────────────────

/**
 * Boot UI previews helper — provides [BootUiStyle] via [LocalUiStyle] to preview functions.
 *
 * **用法示例:**
 * ```kotlin
 * // 1. 只用 UI 风格（无参数）
 * @Preview
 * @Composable
 * @PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
 * fun MyComponentPreview()
 *
 * // 2. UI 风格 + 示例数据
 * @Preview
 * @Composable
 * @PreviewWrapper(wrapper = Material3PreviewWrapper::class)
 * fun MyListPreview(
 *     @PreviewParameter(SampleDataProvider::class) item: Item
 * )
 *
 * // 3. 多变体预览（Light + Dark）
 * @Preview(name = "Light", group = "Material3")
 * @Preview(name = "Dark", group = "Material3")
 * @Composable
 * @PreviewWrapper(wrapper = Material3PreviewWrapper::class)
 * fun MyCardPreview()
 * ```
 *
 * **为什么不直接用 annotation class？**
 * `@PreviewWrapper` 的 `@Target` 是 `FUNCTION`，不能放在 annotation class 上。
 * 因此 wrapper 注解必须放在 `@Composable fun` 上，而不是 MultiPreview annotation class 上。
 *
 * @see PreviewWrapper
 * @see PreviewWrapperProvider
 * @see PreviewParameter
 */
object BootPreviewUtils
