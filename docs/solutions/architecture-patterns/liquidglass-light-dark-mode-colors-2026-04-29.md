---
title: "LiquidGlass 亮色/暗色模式颜色适配"
date: "2026-04-29"
category: "docs/solutions/architecture-patterns/"
module: "boot-ui"
problem_type: "architecture_pattern"
component: "liquidglass"
severity: "medium"
tags: [compose, liquidglass, color, light-mode, dark-mode, compositionlocal, glassmorphism]
applies_when:
  - "构建支持亮色/暗色主题的 glassmorphism 风格组件库"
  - "组件需要在不同背景色下保持可读性"
  - "设计系统需要中心化的颜色传播机制"
related_components:
  - "ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/liquidglass/LiquidGlassColors.kt"
  - "ui/src/commonMain/kotlin/com/yuanjingtech/boot/app/kmp/ui/preview/BootPreviewUtils.kt"
---

# LiquidGlass 亮色/暗色模式颜色适配

## Context

LiquidGlass UI 组件库最初为深色背景设计，所有文本和图标颜色硬编码为 `Color.White`。这导致在浅色背景下内容完全不可见，且没有任何亮色模式支持。随着组件库需要支持亮色/暗色双主题，亟需建立一套中心化的颜色传播机制。

## Guidance

### 1. 定义颜色数据类

```kotlin
// LiquidGlassColors.kt
data class LiquidGlassColors(
    // 主内容色（文本、图标）
    val content: Color,
    val contentAlpha: Float,
    // 次要内容色
    val secondary: Color,
    val secondaryAlpha: Float,
    // 禁用态
    val disabled: Color,
    val disabledAlpha: Float,
    // 玻璃表面叠加色
    val surface: Color,
    val surfaceAlpha: Float,
    // 边框色
    val surfaceBorder: Color,
    val surfaceBorderAlpha: Float,
    // 选中填充色（Checkbox 等）
    val checkedFill: Color,
    val checkedFillAlpha: Float,
    // 开关滑块色
    val thumb: Color,
) {
    companion object {
        val Dark = LiquidGlassColors(
            content = Color.White,
            contentAlpha = 0.92f,
            secondary = Color.White,
            secondaryAlpha = 0.60f,
            disabled = Color.White,
            disabledAlpha = 0.38f,
            surface = Color.White,
            surfaceAlpha = 0.50f,
            surfaceBorder = Color.White,
            surfaceBorderAlpha = 0.25f,
            checkedFill = Color.White,
            checkedFillAlpha = 0.30f,
            thumb = Color.White,
        )

        val Light = LiquidGlassColors(
            content = Color(0xFF1C1B1F),
            contentAlpha = 0.90f,
            secondary = Color(0xFF1C1B1F),
            secondaryAlpha = 0.60f,
            disabled = Color(0xFF1C1B1F),
            disabledAlpha = 0.38f,
            surface = Color(0xFF1C1B1F),
            surfaceAlpha = 0.08f,
            surfaceBorder = Color(0xFF1C1B1F),
            surfaceBorderAlpha = 0.18f,
            checkedFill = Color(0xFF1C1B1F),
            checkedFillAlpha = 0.12f,
            thumb = Color(0xFF1C1B1F),
        )
    }
}
```

### 2. 通过 CompositionLocal 暴露

```kotlin
val LocalLiquidGlassColors = compositionLocalOf { LiquidGlassColors.Dark }
```

### 3. 创建主题检测 Composable

```kotlin
@Composable
fun LiquidGlassTheme(content: @Composable () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val isLight = scheme.surface.luminance() > 0.5f
    val colors = if (isLight) LiquidGlassColors.Light else LiquidGlassColors.Dark
    CompositionLocalProvider(LocalLiquidGlassColors provides colors) {
        content()
    }
}
```

### 4. 组件更新模式

```kotlin
// 修改前
@Composable
fun LiquidGlassTopAppBar(...) {
    Icon(tint = Color.White, ...)
    Text(color = Color.White, ...)
}

// 修改后
@Composable
fun LiquidGlassTopAppBar(...) {
    val colors = LocalLiquidGlassColors.current
    Icon(tint = colors.content, ...)
    Text(color = colors.content, ...)
}
```

### 5. 预览工具双模式支持

```kotlin
class LiquidGlassPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            MaterialTheme(colorScheme = DarkColorScheme) {
                LiquidGlassTheme { content() }
            }
        }
    }
}

class LiquidGlassLightPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            MaterialTheme(colorScheme = LightColorScheme) {
                LiquidGlassTheme { content() }
            }
        }
    }
}
```

## Why This Matters

玻璃态（glassmorphism）的视觉原理依赖于**半透明叠加层与背景之间的对比**，使玻璃边缘可见：

- **暗色模式**：白色叠加层（0.50 alpha）在深色背景上 → 高对比度边缘，强视觉冲击力
- **亮色模式**：深色叠加层（0.08 alpha）在浅色背景上 → 低透明度深色叠加，产生微妙但可见的边缘

将颜色集中在一个 data class 中，通过 `CompositionLocal` 传播，使 13+ 个组件文件免于散布硬编码颜色，且整套调色板可从单一位置调整。

## When to Apply

此模式适用于：

- 设计系统组件库支持 glassmorphism 或基于叠加层的视觉样式
- 组件在亮色和暗色主题的应用中都有使用
- 视觉效果依赖于半透明表面与渲染内容的对比
- 需避免 `Color.White` 或 `Color.Black` 硬编码散布在组件文件中

## Examples

已应用此模式的组件（13+ 个）：

- `LiquidGlassTopAppBar` / `LiquidGlassBottomAppBar`
- `LiquidGlassFAB`（含 Small/Large/Extended 变体）
- `LiquidGlassNavigationBar` / `LiquidGlassNavRail`
- `LiquidGlassTextField`
- `LiquidGlassSelection`（Checkbox / RadioButton / Switch / Slider）
- `LiquidGlassChips`（Assist / Filter / Suggestion）
- `LiquidGlassTabRow`
- `LiquidGlassModalBottomSheet`
- `LiquidGlassDialogs`（AlertDialog / Snackbar）
- `LiquidGlassSearchBar`
- `LiquidGlassDividers`（Horizontal / Vertical）
- `LiquidGlassBadge`

## Related

- [PreviewWrapper API 误用导致预览无法渲染](docs/solutions/ui-bugs/preview-wrapper-api-misuse-2026-04-26.md) — `@PreviewWrapper` / `@PreviewWrapperProvider` 正确用法，`LiquidGlassPreviewWrapper` 的实现细节
- [BootThemeStore crashes on first launch due to nullable Flow](docs/solutions/runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md) — `BootThemeMode`（`FOLLOW_SYSTEM` 等）持久化，与本方案的 `MaterialTheme.colorScheme` 自动检测互补
