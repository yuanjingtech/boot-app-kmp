---
title: "PreviewWrapper API 误用导致预览无法渲染"
date: "2026-04-26"
category: "docs/solutions/ui-bugs/"
module: "boot-ui"
problem_type: "ui_bug"
component: "documentation"
symptoms:
  - "@PreviewWrapperProvider 实现类缺少 `Wrap` 方法或方法签名错误"
  - "@PreviewWrapper 注解放置在 annotation class 上而非 @Composable 函数上"
  - 预览面板显示 "No Preview Found" 或组件空白"
  - "PreviewWrapperProvider 无法正确提供主题和样式环境"
root_cause: "inadequate_documentation"
resolution_type: "code_fix"
severity: "medium"
tags: [compose, preview, previewwrapper, preview-api]
---

# PreviewWrapper API 误用导致预览无法渲染

## Problem

17 个 Boot UI 组件文件中的 `@PreviewWrapper` / `@PreviewWrapperProvider` 用法存在多处错误，导致 Android Studio/IntelliJ 预览面板无法渲染组件，预览显示空白或提示 "No Preview Found"。

## Symptoms

- `@PreviewWrapperProvider` 实现类未重写 `Wrap` 方法，或方法签名写成 `WrapContent` 而非 `Wrap`
- `@PreviewWrapper` 注解放置在自定义 annotation class 上（Annotation Target 不支持），而非 `@Composable fun` 上
- 预览函数使用 `MaterialTheme { }` 和 `CompositionLocalProvider` 双重包装，与 `PreviewWrapperProvider` 重复
- 预览注解参数传递方式错误（传递 `MyWrapper::class` 而非 `MyWrapper` 实例）

## What Didn't Work

- **尝试使用 annotation class**：在 annotation class 上添加 `@PreviewWrapper(wrapper = MyWrapper::class)` 是无效的，因为 `@Target(FUNCTION)` 不允许放在注解声明上。Compose 编译器要求 `@PreviewWrapper` 只能直接在 `@Composable fun` 上使用。
- **错误的方法名**：将 `override fun Wrap` 误写为 `override fun WrapContent`，导致接口方法未实现。
- **双重包装**：在预览函数内手动添加 `MaterialTheme` 和 `CompositionLocalProvider`，与 `PreviewWrapperProvider` 提供的环境冲突或重复。

## Solution

### 1. 正确的 PreviewWrapperProvider 实现

```kotlin
// ✅ 正确：实现 PreviewWrapperProvider 并使用正确的方法名
class LiquidGlassPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {
            MaterialTheme {
                content()
            }
        }
    }
}
```

```kotlin
// ❌ 错误：方法名错误
class LiquidGlassPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun WrapContent(content: @Composable () -> Unit) {  // 错误名称
        // ...
    }
}
```

### 2. 预览函数直接使用 @PreviewWrapper

```kotlin
// ✅ 正确：预览函数同时使用 @Preview 和 @PreviewWrapper
@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootButtonLiquidGlassPreview() {
    BootButton(text = "Click me", onClick = {})
}
```

```kotlin
// ❌ 错误：在 annotation class 上使用 @PreviewWrapper
@StylePreviews  // ❌ annotation class 上不能放 @PreviewWrapper
annotation class StylePreviews

@Preview(name = "LiquidGlass")
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)  // 只能放在 fun 上
@Composable
private fun BootButtonLiquidGlassPreview()
```

### 3. 预览函数内部不再需要手动包装

```kotlin
// ✅ 正确：PreviewWrapperProvider 已提供 MaterialTheme 和 LocalUiStyle
@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootButtonLiquidGlassPreview() {
    BootButton(text = "Click me", onClick = {})
}
```

```kotlin
// ❌ 错误：双重包装（多余且可能引起冲突）
@Preview
@Composable
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
private fun BootButtonLiquidGlassPreview() {
    MaterialTheme {  // ❌ PreviewWrapperProvider 已提供
        CompositionLocalProvider(LocalUiStyle provides BootUiStyle.LIQUID_GLASS) {  // ❌ 重复
            BootButton(text = "Click me", onClick = {})
        }
    }
}
```

### 4. @PreviewWrapper 参数传递方式

```kotlin
// ✅ 正确：传递 Kotlin class 实例（::class 后跟实例化）
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)

// ✅ 也可传递 KClass（取决于 API 版本）
@PreviewWrapper(wrapper = LiquidGlassPreviewWrapper::class)
```

> **注意**：不同 Compose 版本对 `@PreviewWrapper` 的参数类型要求不同。compose 1.11+ 使用 `KClass`，传入 `MyWrapper::class`；部分版本要求传入实例。

## Why This Works

`PreviewWrapperProvider` 是 Compose 1.11+ 引入的 API，通过 `PreviewWrapper` 注解将预览环境（主题、样式、语言等）注入到预览函数中。关键约束：

1. **`@Target(FUNCTION)` 限制**：`@PreviewWrapper` 只能直接放在 `@Composable fun` 上，不能放在注解声明上。这是由 Compose 编译器插件的 AST 转换逻辑决定的。
2. **`Wrap` 而非 `WrapContent`**：`PreviewWrapperProvider` 接口的方法名是固定的 `Wrap`，实现时必须精确匹配，否则接口方法不会被重写（Kotlin 默认行为：未重写的方法不参与调度）。
3. **`PreviewWrapperProvider` 提供完整环境**：实现类负责提供 `MaterialTheme` + `LocalUiStyle`，预览函数内部无需重复包装。

## Prevention

1. **始终在 `@Composable fun` 上直接使用 `@PreviewWrapper`**，不要通过中间 annotation class 间接使用
2. **实现 `PreviewWrapperProvider` 时检查方法签名**：必须是 `override fun Wrap(content: @Composable () -> Unit)`
3. **预览函数内部只调用待预览组件**，不额外包装主题或样式
4. **验证预览渲染**：提交前在 Android Studio/IntelliJ 中确认预览正常显示

## Related Issues

- [runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md](/docs/solutions/runtime-errors/theme-settings-null-flow-runtime-crash-2026-04-24.md)
